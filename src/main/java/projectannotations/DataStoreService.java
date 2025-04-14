package projectannotations;

import datastore.DataProcessingGrpc;
import datastore.DatastoreProto;
import io.grpc.stub.StreamObserver;
import datastorecomponents.DataProcessingAPI;
import datastorecomponents.DataProcessingImp;
import datastorecomponents.FileInputConfig;
import datastorecomponents.FileOutputConfig;
import datastorecomponents.InputConfig;
import datastorecomponents.OutputConfig;
import datastorecomponents.ReadResult;
import datastorecomponents.WriteResult;

public class DataStoreService extends DataProcessingGrpc.DataProcessingImplBase {
    private final DataProcessingAPI api = new DataProcessingImp();

    @Override
    public void read(DatastoreProto.InputConfig request, StreamObserver<DatastoreProto.ReadResult> responseObserver) {
        InputConfig input = new FileInputConfig(request.getFilePath());
        ReadResult result = api.read(input);

        DatastoreProto.ReadResult.Builder grpcResultBuilder = DatastoreProto.ReadResult.newBuilder()
                .setStatus(result.getStatus() == ReadResult.Status.SUCCESS
                        ? DatastoreProto.ReadResult.Status.SUCCESS
                        : DatastoreProto.ReadResult.Status.FAILURE);
        
        for (Integer i : result.getResults()) {
            grpcResultBuilder.addData(i);
        }

        responseObserver.onNext(grpcResultBuilder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void appendSingleResult(DatastoreProto.AppendRequest request, StreamObserver<DatastoreProto.WriteResult> responseObserver) {
        OutputConfig output = new FileOutputConfig(request.getOutput().getFilePath());
        WriteResult result = api.appendSingleResult(output, request.getResult(), request.getDelimiter().charAt(0));

        DatastoreProto.WriteResult grpcResult = DatastoreProto.WriteResult.newBuilder()
                .setStatus(result.getStatus() == WriteResult.WriteResultStatus.SUCCESS
                        ? DatastoreProto.WriteResult.WriteResultStatus.SUCCESS
                        : DatastoreProto.WriteResult.WriteResultStatus.FAILURE)
                .build();

        responseObserver.onNext(grpcResult);
        responseObserver.onCompleted();
    }
}
