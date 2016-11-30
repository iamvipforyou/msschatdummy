package com.mss.msschat.Interfaces;

import com.mss.msschat.Models.ContactResponse;
import com.mss.msschat.Models.RegistrationModel;
import com.mss.msschat.Models.RegistrationResponse;
import com.mss.msschat.Models.VerifyContactsModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by mss on 25/11/16.
 */

public interface ApiInterface {


    @POST("api/registration")
    Call<RegistrationResponse> getRegistrationResponse(@Body RegistrationModel registrationData);

    @POST("api/importContacts")
    Call<ContactResponse> getAllAppContactResponse(@Body VerifyContactsModel verifyContactsModel);


}
