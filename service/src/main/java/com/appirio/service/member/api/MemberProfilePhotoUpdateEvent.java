package com.appirio.service.member.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Represents Member profile photo update event
 *
 * Created by prasadsadhu on 8/25/15.
 */
@AllArgsConstructor
@NoArgsConstructor
public class MemberProfilePhotoUpdateEvent {

    /**
     * Id of the user
     */
    @Getter
    @Setter
    private Integer userId;

    /**
     * Photo URL
     */
    @Getter
    @Setter
    private String photoURL;
}