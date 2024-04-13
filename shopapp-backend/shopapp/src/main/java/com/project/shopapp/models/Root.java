package com.project.shopapp.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.SQLInsert;

import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Root{
    @JsonProperty("at_hash")
    public String atHash;
    @JsonProperty("sub")
    public String sub;
    @JsonProperty("email_verified")
    public boolean email_verified;
    @JsonProperty("iss")
    public String iss;
    @JsonProperty("given_name")
    public String givenName;
    @JsonProperty("locale")
    public String locale;
    @JsonProperty("nonce")
    public String nonce;
    @JsonProperty("picture")
    public String picture;
    @JsonProperty("aud")
    public ArrayList<String> aud;
    @JsonProperty("azp")
    public String azp;
    @JsonProperty("name")
    public String name;
    @JsonProperty("exp")
    public int exp;
    @JsonProperty("family_name")
    public String familyName;
    @JsonProperty("iat")
    public int iat;
    @JsonProperty("email")
    public String email;
}


