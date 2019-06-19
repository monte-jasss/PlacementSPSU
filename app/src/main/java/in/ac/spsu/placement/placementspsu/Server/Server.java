package in.ac.spsu.placement.placementspsu.Server;

import java.util.List;

import in.ac.spsu.placement.placementspsu.Model.CandidateData;
import in.ac.spsu.placement.placementspsu.Model.FeedJobData;
import in.ac.spsu.placement.placementspsu.Model.ListJobData;
import in.ac.spsu.placement.placementspsu.Model.RequestData;
import in.ac.spsu.placement.placementspsu.Model.UserData;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Server {
    @FormUrlEncoded
    @POST("login.php")
    Call<String> getAuthentication(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("register.php")
    Call<String> doUserRegistration(
            @Field("name") String name,
            @Field("email") String email,
            @Field("mobile") Long mobile,
            @Field("password") String password,
            @Field("type") int type
    );

    @FormUrlEncoded
    @POST("otpVerify.php")
    Call<String> getOTPVerification(
            @Field("email") String email,
            @Field("otp") int otp
    );

    @FormUrlEncoded
    @POST("changePassword.php")
    Call<String> changePassword(
            @Field("email") String email,
            @Field("password") String password
    );

    @FormUrlEncoded
    @POST("resend.php")
    Call<String> resendOTP(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("addjob.php")
    Call<String> doAddJob(
            @Field("email") String email,
            @Field("title") String title,
            @Field("description") String description,
            @Field("requirement") String requirement,
            @Field("opening") String opening,
            @Field("lastdate") String lastdate
    );

    @FormUrlEncoded
    @POST("companyJobList.php")
    Call<List<ListJobData>> getAllCurrentCompanyJob(@Field("email") String email);

    @FormUrlEncoded
    @POST("allJobList.php")
    Call<List<FeedJobData>> getAllCompanyJob(@Field("email") String email);

    @FormUrlEncoded
    @POST("updateProfile.php")
    Call<String> updateStudentProfile(
            @Field("email") String email,
            @Field("name") String name,
            @Field("fname") String fname,
            @Field("course") String course,
            @Field("semester") String semester,
            @Field("_10") String _10,
            @Field("_12") String _12,
            @Field("cgpa") String cgpa,
            @Field("interest") String interest,
            @Field("skill") String skill,
            @Field("project") String project,
            @Field("training") String training,
            @Field("certificate") String certificate
    );

    @FormUrlEncoded
    @POST("getProfile.php")
    Call<List<String>> getStudentProfile(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getAllUserByType.php")
    Call<List<UserData>> getAllUserByType(
            @Field("type") int type
    );

    @FormUrlEncoded
    @POST("getProfileById.php")
    Call<List<String>> getStudentProfileById(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("getJobDetail.php")
    Call<FeedJobData> getJobDetails(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("applyForJob.php")
    Call<String> doApplyForJob(
            @Field("job_id") int jobId,
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getMyRequest.php")
    Call<List<RequestData>> getAppliedJobList(
            @Field("email") String email
    );

    @FormUrlEncoded
    @POST("getMyApplication.php")
    Call<List<CandidateData>> getApplicationList(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("deleteMyJob.php")
    Call<String> doRemoveJob(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("deleteUser.php")
    Call<String> doRemoveUser(
            @Field("id") int id,
            @Field("type") int type
    );

    @FormUrlEncoded
    @POST("shortlist.php")
    Call<String> sendStatus(
            @Field("status") int status,
            @Field("id") int id
    );
}
