package com.githubx.usersms.grpc;

import com.githubx.grpc.proto.PaginationMeta;
import com.githubx.grpc.proto.UserDTO;
import com.githubx.usersms.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class GrpcProtoMapper {

    public UserDTO toProtoUser(User user) {
        return UserDTO.newBuilder()
                .setUserId(safe(user.getUserId()))
                .setEmail(safe(user.getEmail()))
                .setUsername(safe(user.getUsername()))
                .setFirstName(safe(user.getFirstName()))
                .setLastName(safe(user.getLastName()))
                .setStatus(safeInt(user.getStatus()))
                .setCreatedDate(user.getCreatedDate() != null ? user.getCreatedDate().toInstant().toString() : "")
                .setModifiedDate(user.getModifiedDate() != null ? user.getModifiedDate().toInstant().toString() : "")
                .build();
    }

    public PaginationMeta toProtoPagination(Page<?> page) {
        return PaginationMeta.newBuilder()
                .setPage((int) page.getNumber())
                .setPerPage(page.getSize())
                .setTotal((int) page.getTotalElements())
                .setTotalPages(page.getTotalPages())
                .build();
    }

    private String safe(String s) {
        return s != null ? s : "";
    }

    private int safeInt(Integer i) {
        return i != null ? i : 0;
    }
}
