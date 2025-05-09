package projectannotations;


import projectannotations.NetworkAPI;
import io.grpc.stub.StreamObserver;
import networkapi.NetworkAPIProto;


/**
 * Interface for the gRPC network layer.
 */
@NetworkAPI
public interface GrpcNetworkAPI {
    void processRequest(NetworkAPIProto.RequestMessage request,
                        StreamObserver<NetworkAPIProto.ResponseMessage> responseObserver);
}
