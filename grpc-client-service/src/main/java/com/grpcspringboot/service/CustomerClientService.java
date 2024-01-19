package com.grpcspringboot.service;


import com.customerservice.grpc.stubs.CustomerAccountDetails;
import com.customerservice.grpc.stubs.CustomerDetails;
import com.customerservice.grpc.stubs.CustomerDetailsRequest;
import com.customerservice.grpc.stubs.CustomerServiceGrpc;
import com.google.protobuf.Descriptors;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
public class CustomerClientService {

    @GrpcClient("customer-service")
    private CustomerServiceGrpc.CustomerServiceBlockingStub customerServiceBlockingStub; //synchronous client
    @GrpcClient("customer-service")
    private CustomerServiceGrpc.CustomerServiceStub customerServiceStub; //asynchronous client

    public Map<Descriptors.FieldDescriptor, Object> getCustomerDetails(String customerId) {
        CustomerDetailsRequest customerDetailsRequest = CustomerDetailsRequest.newBuilder()
                .setIdValue(customerId).build();  // when creating a proto-generated object we cannot use the constructor.We have to use the newBuilder() method

        return customerServiceBlockingStub.getCustomerDetails(customerDetailsRequest).getAllFields();

    }


    public List<Map<Descriptors.FieldDescriptor, Object>> getCustomerAccountDetails(CustomerDetails customerDetails) throws InterruptedException {
        final CountDownLatch awaitCountDown = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        customerServiceStub.getCustomerAccounts(customerDetails, new StreamObserver<CustomerAccountDetails>() {
            @Override
            public void onNext(com.customerservice.grpc.stubs.CustomerAccountDetails customerAccountDetails) {
                response.add(customerAccountDetails.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {

                }
        });

        return awaitCountDown.await(1, TimeUnit.MINUTES)? response : Collections.emptyList();

    }

    public Map<String, Map<Descriptors.FieldDescriptor, Object>> getNewestAccount(List<CustomerAccountDetails> customerAccountDetailsList) throws InterruptedException {
        final CountDownLatch awaitCountDown = new CountDownLatch(1);
        final Map<String, Map<Descriptors.FieldDescriptor, Object>> response = new java.util.HashMap<>();
        StreamObserver<CustomerAccountDetails> responseObserver = customerServiceStub.getNewestAccount(new StreamObserver<CustomerAccountDetails>() {
            @Override
            public void onNext(CustomerAccountDetails customerAccountDetails) { //on next response(there will be only one response)
                response.put("newestAccount", customerAccountDetails.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {

            }


            @Override
            public void onCompleted() {
            }
        });

        customerAccountDetailsList.forEach(responseObserver::onNext);
        responseObserver.onCompleted();

        return awaitCountDown.await(1, TimeUnit.MINUTES) ? response : Collections.emptyMap();
    }

    public List<Map<Descriptors.FieldDescriptor, Object>> getAccountsOlderThanOneYear(List<CustomerAccountDetails> customerAccountDetailsList) throws InterruptedException {
        final CountDownLatch awaitCountDown = new CountDownLatch(1);
        final List<Map<Descriptors.FieldDescriptor, Object>> response = new ArrayList<>();
        StreamObserver<CustomerAccountDetails> responseObserver = customerServiceStub.getAccountsOlderThanOneYear(new StreamObserver<CustomerAccountDetails>() {
            @Override
            public void onNext(CustomerAccountDetails customerAccountDetails) { //on next response
                response.add(customerAccountDetails.getAllFields());
            }

            @Override
            public void onError(Throwable throwable) {

            }

            @Override
            public void onCompleted() {
            }
        });

        customerAccountDetailsList.forEach(responseObserver::onNext);
        responseObserver.onCompleted();

        return awaitCountDown.await(1, TimeUnit.MINUTES) ? response : Collections.emptyList();

    }
}
