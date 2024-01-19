package com.grpcspringboot.service;


import com.customerservice.grpc.stubs.CustomerAccountDetails;
import com.customerservice.grpc.stubs.CustomerDetails;
import com.customerservice.grpc.stubs.CustomerDetailsRequest;
import com.customerservice.grpc.stubs.CustomerServiceGrpc;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@GrpcService
public class CustomerServiceImpl extends CustomerServiceGrpc.CustomerServiceImplBase {
    @Override
    public void getCustomerDetails(CustomerDetailsRequest request, StreamObserver<CustomerDetails> responseObserver) {
        TempData.getCustomerDetailsFromTempDb()
                .stream()
                .filter(customerDetails -> customerDetails.getIdValue().equals(request.getIdValue()))
                .findFirst()
                .ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();

     }

    @Override
    public void getCustomerAccounts(CustomerDetails request, StreamObserver<CustomerAccountDetails> responseObserver) {
         TempData.getCustomerAccountDetailsFromTempDb()
                 .stream()
                 .filter(accountDetails -> accountDetails.getCustomerId().equals(request.getIdValue()))
                 .findFirst()
                 .ifPresent(responseObserver::onNext); // onNext will send each book called in foreach loop real time to the client without waiting for the loop to be completed
         responseObserver.onCompleted();
     }

    @Override

    public StreamObserver<CustomerAccountDetails> getNewestAccount(StreamObserver<CustomerAccountDetails> responseObserver) {
        return new StreamObserver<CustomerAccountDetails>() {
            CustomerAccountDetails newestAccount = null;
            long newestTimeInSecs = 0;

            @Override
            public void onNext(CustomerAccountDetails customerAccountDetails) { //consume the customerAccountDetails objects streamed in request
                if (customerAccountDetails.getAccountCreationDate().getSeconds() < newestTimeInSecs
                        || newestTimeInSecs == 0) {
                    newestTimeInSecs = customerAccountDetails.getAccountCreationDate().getSeconds();
                    newestAccount = customerAccountDetails;
                }
            }

                @Override
                public void onError(Throwable throwable) {
                    responseObserver.onError(throwable);
                }

                @Override
                public void onCompleted() { //invoked when the client send the acknowledgement that streaming of all the books in the request is completed
                    responseObserver.onNext(newestAccount); //send the newestAccount to the client
                    responseObserver.onCompleted();
                }
            };
    }

    @Override
    public StreamObserver<CustomerAccountDetails> getAccountsOlderThanOneYear(StreamObserver<CustomerAccountDetails> responseObserver) {
        return new StreamObserver<CustomerAccountDetails>() {
            final CustomerAccountDetails oldestAccount = null;

            final long secondsPerYear = 31536000;
            final long yearAgoInSecs = Instant.now().getEpochSecond() - secondsPerYear;

            final List<CustomerAccountDetails> olderAccounts = new ArrayList<>();
            @Override
            public void onNext(CustomerAccountDetails customerAccountDetails) {
                if (customerAccountDetails.getAccountCreationDate().getSeconds() < yearAgoInSecs) {
                    olderAccounts.add(customerAccountDetails);
                }
            }

                @Override
                public void onError(Throwable throwable) {
                    responseObserver.onError(throwable);
                }

                @Override
                public void onCompleted() {
                    responseObserver.onNext(oldestAccount);
                    responseObserver.onCompleted();
                }
            };
    }



}
