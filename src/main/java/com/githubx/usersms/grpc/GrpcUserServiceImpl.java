package com.githubx.usersms.grpc;

import com.githubx.grpc.proto.*;
import com.githubx.usersms.dto.request.UserRequest;
import com.githubx.usersms.model.Transaction;
import com.githubx.usersms.service.contratos.UserService;
import com.githubx.usersms.util.errorhandling.exceptions.EntityConflictException;
import com.githubx.usersms.util.errorhandling.exceptions.EntityNotFoundException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Date;

@GrpcService
@RequiredArgsConstructor
public class GrpcUserServiceImpl extends UserApiGrpc.UserApiImplBase {

    private final UserService userService;
    private final GrpcProtoMapper mapper;

    @Override
    public void listUsers(ListUsersRequest req, StreamObserver<ListUsersResponse> obs) {
        try {
            int page = req.getPagination().getPage() > 0 ? req.getPagination().getPage() : 0;
            int perPage = req.getPagination().getPerPage() > 0 ? req.getPagination().getPerPage() : 10;
            var result = userService.listUsers(page, perPage);
            obs.onNext(ListUsersResponse.newBuilder()
                    .addAllUsers(result.stream().map(mapper::toProtoUser).toList())
                    .setPagination(mapper.toProtoPagination(result))
                    .build());
            obs.onCompleted();
        } catch (Exception e) {
            obs.onError(toStatus(e).asRuntimeException());
        }
    }

    @Override
    public void createUser(CreateUserRequest req, StreamObserver<CreateUserResponse> obs) {
        try {
            var request = new UserRequest();
            request.setEmail(req.getEmail());
            request.setUsername(req.getUsername());
            request.setFirstName(req.getFirstName());
            request.setLastName(req.getLastName());
            request.setPassword(req.getPassword());
            obs.onNext(CreateUserResponse.newBuilder()
                    .setUser(mapper.toProtoUser(userService.createUser(request, buildTransaction())))
                    .build());
            obs.onCompleted();
        } catch (Exception e) {
            obs.onError(toStatus(e).asRuntimeException());
        }
    }

    @Override
    public void editUser(EditUserRequest req, StreamObserver<EditUserResponse> obs) {
        try {
            var request = new UserRequest();
            if (!req.getEmail().isEmpty()) request.setEmail(req.getEmail());
            if (!req.getUsername().isEmpty()) request.setUsername(req.getUsername());
            if (!req.getFirstName().isEmpty()) request.setFirstName(req.getFirstName());
            if (!req.getLastName().isEmpty()) request.setLastName(req.getLastName());
            if (!req.getPassword().isEmpty()) request.setPassword(req.getPassword());
            obs.onNext(EditUserResponse.newBuilder()
                    .setUser(mapper.toProtoUser(userService.editUser(req.getUserId(), request, buildTransaction())))
                    .build());
            obs.onCompleted();
        } catch (Exception e) {
            obs.onError(toStatus(e).asRuntimeException());
        }
    }

    @Override
    public void deleteUser(DeleteUserRequest req, StreamObserver<DeleteUserResponse> obs) {
        try {
            var result = userService.deleteUser(req.getUserId());
            obs.onNext(DeleteUserResponse.newBuilder()
                    .setSuccess(true)
                    .setMessage(result != null && result.getMessage() != null ? result.getMessage() : "User deleted")
                    .build());
            obs.onCompleted();
        } catch (Exception e) {
            obs.onError(toStatus(e).asRuntimeException());
        }
    }

    // ─── Helpers ──────────────────────────────────────────────

    private Transaction buildTransaction() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String userId = (auth != null) ? auth.getName() : "grpc";
        return Transaction.builder()
                .trUserId(userId)
                .trFecha(new Date())
                .trHost("grpc")
                .build();
    }

    private Status toStatus(Exception e) {
        if (e instanceof EntityNotFoundException) return Status.NOT_FOUND.withDescription(e.getMessage());
        if (e instanceof EntityConflictException) return Status.ALREADY_EXISTS.withDescription(e.getMessage());
        return Status.INTERNAL.withDescription(e.getMessage());
    }
}
