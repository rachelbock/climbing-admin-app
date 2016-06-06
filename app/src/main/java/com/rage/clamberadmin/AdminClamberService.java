package com.rage.clamberadmin;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * interface to hold network calls used with Retrofit
 */

public interface AdminClamberService {

    @GET("admin/admin_user/{username}")
    Call<Boolean> getAdminUser(@Path("username") String username);

    @GET("admin/wall/{wall_id}/wall_sections")
    Call<List<WallSection>> getWallSection(@Path("wall_id") int wallId);

    @POST("admin/wall/{wall_id}/wall_sections/{wall_section_id}/remove_climbs")
    Call<Boolean> removeClimbsFromWallSection(@Path("wall_id") int wallId, @Path("wall_section_id") int wallSectionId);

    @GET("admin/wall/{wall_id}/wall_sections/{wall_section_id}/climbs")
    Call<List<Climb>> getClimbsByWallSection(@Path("wall_id") int wallId, @Path("wall_section_id") int wallSectionId);

    @POST("admin/wall/{wall_id}/wall_sections/{wall_section_id}/add_climb")
    Call<Boolean> addNewClimb(@Path("wall_id") int wallId, @Path("wall_section_id") int wallSectionId, @Body NewClimbRequest request);

    @POST("admin/wall/{wall_id}/wall_sections/{wall_section_id}/climbs/{climb_id}/remove")
    Call<Boolean> removeSingleClimb(@Path("wall_id") int wallId, @Path("wall_section_id") int wallSectionId, @Path("climb_id") int climbId);

}
