package com.mss.msschat.Interfaces;

import com.mss.msschat.Models.AddMemberModel;
import com.mss.msschat.Models.AddingMemberResponse.AddMemberResponse;
import com.mss.msschat.Models.ContactResponse;
import com.mss.msschat.Models.CreateGroupModel;
import com.mss.msschat.Models.CreateGroupResponse;
import com.mss.msschat.Models.DeleteGroupModel;
import com.mss.msschat.Models.DeleteGroupResponse;
import com.mss.msschat.Models.GroupUserResponse.GroupUsersResponse;
import com.mss.msschat.Models.RegistrationModel;
import com.mss.msschat.Models.RegistrationResponse;
import com.mss.msschat.Models.RemoveGroupUserResponse.RemoveGroupUserResponse;
import com.mss.msschat.Models.RemoveGrpUserModel;
import com.mss.msschat.Models.VerifyContactsModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by mss on 25/11/16.
 */

public interface ApiInterface {


    @POST("api/registration")
    Call<RegistrationResponse> getRegistrationResponse(@Body RegistrationModel registrationData);

    @POST("api/importContacts")
    Call<ContactResponse> getAllAppContactResponse(@Body VerifyContactsModel verifyContactsModel);

    @POST("api/addGroup")
    Call<CreateGroupResponse> getCreateGroupDetails(@Body CreateGroupModel createGroupModel);


    @POST("api/addGroupUsers")
    Call<AddMemberResponse> getAddSelectedMemberResponse(@Body AddMemberModel addMemberModel);

    @GET("api/listGroupUsers/{groupId}")
    Call<GroupUsersResponse> getAllGroupUsers(@Path("groupId") String GroupId);

    @POST("api/deleteGroup")
    Call<DeleteGroupResponse> deleteGroupFromServer(@Body DeleteGroupModel deleteGroupModel);


    @POST("api/removeGroupUsers")
    Call<RemoveGroupUserResponse> removeUserFromGrp(@Body RemoveGrpUserModel removeGrpUserModel);


}
