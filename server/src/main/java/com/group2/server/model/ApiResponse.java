// server/src/main/java/com/group2/server/model/ApiResponse.java
package com.group2.server.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;
}