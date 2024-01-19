package com.grpcspringboot.service;

import com.customerservice.grpc.stubs.CustomerAccountDetails;
import com.customerservice.grpc.stubs.CustomerDetails;
import com.customerservice.grpc.stubs.Gender;
import com.customerservice.grpc.stubs.IdType;
import com.google.protobuf.Timestamp;

import java.util.ArrayList;
import java.util.List;

public class TempData {
    public static List<CustomerDetails> getCustomerDetailsFromTempDb() {
        return new ArrayList<CustomerDetails>() {
            {
                add(CustomerDetails.newBuilder().setIdType(IdType.NRIC).setIdValue("123564").setName("Amal").setAge(23)
                        .setGender(Gender.MALE).build());
                add(CustomerDetails.newBuilder().setIdType(IdType.NRIC).setIdValue("523564").setName("Nimali").setAge(26)
                        .setGender(Gender.FEMALE).build());
                add(CustomerDetails.newBuilder().setIdType(IdType.NRIC).setIdValue("723564").setName("Manel").setAge(47)
                        .setGender(Gender.FEMALE).build());
                add(CustomerDetails.newBuilder().setIdType(IdType.PASSPORT).setIdValue("452323564").setName("Shamal").setAge(64)
                        .setGender(Gender.MALE).build());
                add(CustomerDetails.newBuilder().setIdType(IdType.BRN).setIdValue("923564").setName("Nimesha").setAge(32)
                        .setGender(Gender.FEMALE).build());
            }


        };
    }

    public static List<CustomerAccountDetails> getCustomerAccountDetailsFromTempDb() {
        return new ArrayList<CustomerAccountDetails>() {
            {
                add(CustomerAccountDetails.newBuilder().setAccountName("Amal").setCustomerId("123564").setBillingAccountNumber(432123564)
                        .setMsisdn(123564).setAccountCreationDate(Timestamp.newBuilder().setSeconds(1705060455).build()).build());
                add(CustomerAccountDetails.newBuilder().setAccountName("Nimali").setCustomerId("523564").setBillingAccountNumber(756523564)
                        .setMsisdn(523564).setAccountCreationDate(Timestamp.newBuilder().setSeconds(1605060455).build()).build());
                add(CustomerAccountDetails.newBuilder().setAccountName("Manel").setCustomerId("723564").setBillingAccountNumber(867723564)
                        .setMsisdn(723564).setAccountCreationDate(Timestamp.newBuilder().setSeconds(1703060455).build()).build());
                add(CustomerAccountDetails.newBuilder().setAccountName("Shamal").setCustomerId("452323564").setBillingAccountNumber(742323564)
                        .setMsisdn(452323564).setAccountCreationDate(Timestamp.newBuilder().setSeconds(1705050455).build()).build());
                add(CustomerAccountDetails.newBuilder().setAccountName("Nimesha").setCustomerId("923564").setBillingAccountNumber(234923564)
                        .setMsisdn(923564).setAccountCreationDate(Timestamp.newBuilder().setSeconds(1505060455).build()).build());
            }
        };
    }



}
