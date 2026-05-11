package com.githubx.usersms.grpc;

import com.githubx.grpc.proto.GetUserRequest;
import com.githubx.grpc.proto.GetUserResponse;
import com.githubx.grpc.proto.UserPublicApiGrpc;
import com.githubx.usersms.service.contratos.UserService;
import com.githubx.usersms.util.errorhandling.exceptions.EntityNotFoundException;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
@RequiredArgsConstructor
public class GrpcUserPublicServiceImpl extends UserPublicApiGrpc.UserPublicApiImplBase {

    private final UserService userService;
    private final GrpcProtoMapper mapper;

    @Override
    public void getUser(GetUserRequest req, StreamObserver<GetUserResponse> obs) {
        try {
            obs.onNext(GetUserResponse.newBuilder()
                    .setUser(mapper.toProtoUser(userService.getUser(req.getUserId())))
                    .build());
            obs.onCompleted();
        } catch (EntityNotFoundException e) {
            obs.onError(Status.NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (Exception e) {
            obs.onError(Status.INTERNAL.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}
