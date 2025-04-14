package datastore;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.62.2)",
    comments = "Source: datastore.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class DataProcessingGrpc {

  private DataProcessingGrpc() {}

  public static final java.lang.String SERVICE_NAME = "datastore.DataProcessing";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<datastore.DatastoreProto.InputConfig,
      datastore.DatastoreProto.ReadResult> getReadMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "Read",
      requestType = datastore.DatastoreProto.InputConfig.class,
      responseType = datastore.DatastoreProto.ReadResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<datastore.DatastoreProto.InputConfig,
      datastore.DatastoreProto.ReadResult> getReadMethod() {
    io.grpc.MethodDescriptor<datastore.DatastoreProto.InputConfig, datastore.DatastoreProto.ReadResult> getReadMethod;
    if ((getReadMethod = DataProcessingGrpc.getReadMethod) == null) {
      synchronized (DataProcessingGrpc.class) {
        if ((getReadMethod = DataProcessingGrpc.getReadMethod) == null) {
          DataProcessingGrpc.getReadMethod = getReadMethod =
              io.grpc.MethodDescriptor.<datastore.DatastoreProto.InputConfig, datastore.DatastoreProto.ReadResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Read"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreProto.InputConfig.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreProto.ReadResult.getDefaultInstance()))
              .setSchemaDescriptor(new DataProcessingMethodDescriptorSupplier("Read"))
              .build();
        }
      }
    }
    return getReadMethod;
  }

  private static volatile io.grpc.MethodDescriptor<datastore.DatastoreProto.AppendRequest,
      datastore.DatastoreProto.WriteResult> getAppendSingleResultMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "AppendSingleResult",
      requestType = datastore.DatastoreProto.AppendRequest.class,
      responseType = datastore.DatastoreProto.WriteResult.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<datastore.DatastoreProto.AppendRequest,
      datastore.DatastoreProto.WriteResult> getAppendSingleResultMethod() {
    io.grpc.MethodDescriptor<datastore.DatastoreProto.AppendRequest, datastore.DatastoreProto.WriteResult> getAppendSingleResultMethod;
    if ((getAppendSingleResultMethod = DataProcessingGrpc.getAppendSingleResultMethod) == null) {
      synchronized (DataProcessingGrpc.class) {
        if ((getAppendSingleResultMethod = DataProcessingGrpc.getAppendSingleResultMethod) == null) {
          DataProcessingGrpc.getAppendSingleResultMethod = getAppendSingleResultMethod =
              io.grpc.MethodDescriptor.<datastore.DatastoreProto.AppendRequest, datastore.DatastoreProto.WriteResult>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "AppendSingleResult"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreProto.AppendRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  datastore.DatastoreProto.WriteResult.getDefaultInstance()))
              .setSchemaDescriptor(new DataProcessingMethodDescriptorSupplier("AppendSingleResult"))
              .build();
        }
      }
    }
    return getAppendSingleResultMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static DataProcessingStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataProcessingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataProcessingStub>() {
        @java.lang.Override
        public DataProcessingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataProcessingStub(channel, callOptions);
        }
      };
    return DataProcessingStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static DataProcessingBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataProcessingBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataProcessingBlockingStub>() {
        @java.lang.Override
        public DataProcessingBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataProcessingBlockingStub(channel, callOptions);
        }
      };
    return DataProcessingBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static DataProcessingFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<DataProcessingFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<DataProcessingFutureStub>() {
        @java.lang.Override
        public DataProcessingFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new DataProcessingFutureStub(channel, callOptions);
        }
      };
    return DataProcessingFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void read(datastore.DatastoreProto.InputConfig request,
        io.grpc.stub.StreamObserver<datastore.DatastoreProto.ReadResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getReadMethod(), responseObserver);
    }

    /**
     */
    default void appendSingleResult(datastore.DatastoreProto.AppendRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreProto.WriteResult> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getAppendSingleResultMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service DataProcessing.
   */
  public static abstract class DataProcessingImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return DataProcessingGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service DataProcessing.
   */
  public static final class DataProcessingStub
      extends io.grpc.stub.AbstractAsyncStub<DataProcessingStub> {
    private DataProcessingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataProcessingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataProcessingStub(channel, callOptions);
    }

    /**
     */
    public void read(datastore.DatastoreProto.InputConfig request,
        io.grpc.stub.StreamObserver<datastore.DatastoreProto.ReadResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void appendSingleResult(datastore.DatastoreProto.AppendRequest request,
        io.grpc.stub.StreamObserver<datastore.DatastoreProto.WriteResult> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getAppendSingleResultMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service DataProcessing.
   */
  public static final class DataProcessingBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<DataProcessingBlockingStub> {
    private DataProcessingBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataProcessingBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataProcessingBlockingStub(channel, callOptions);
    }

    /**
     */
    public datastore.DatastoreProto.ReadResult read(datastore.DatastoreProto.InputConfig request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getReadMethod(), getCallOptions(), request);
    }

    /**
     */
    public datastore.DatastoreProto.WriteResult appendSingleResult(datastore.DatastoreProto.AppendRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getAppendSingleResultMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service DataProcessing.
   */
  public static final class DataProcessingFutureStub
      extends io.grpc.stub.AbstractFutureStub<DataProcessingFutureStub> {
    private DataProcessingFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected DataProcessingFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new DataProcessingFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<datastore.DatastoreProto.ReadResult> read(
        datastore.DatastoreProto.InputConfig request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getReadMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<datastore.DatastoreProto.WriteResult> appendSingleResult(
        datastore.DatastoreProto.AppendRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getAppendSingleResultMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_READ = 0;
  private static final int METHODID_APPEND_SINGLE_RESULT = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_READ:
          serviceImpl.read((datastore.DatastoreProto.InputConfig) request,
              (io.grpc.stub.StreamObserver<datastore.DatastoreProto.ReadResult>) responseObserver);
          break;
        case METHODID_APPEND_SINGLE_RESULT:
          serviceImpl.appendSingleResult((datastore.DatastoreProto.AppendRequest) request,
              (io.grpc.stub.StreamObserver<datastore.DatastoreProto.WriteResult>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getReadMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              datastore.DatastoreProto.InputConfig,
              datastore.DatastoreProto.ReadResult>(
                service, METHODID_READ)))
        .addMethod(
          getAppendSingleResultMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              datastore.DatastoreProto.AppendRequest,
              datastore.DatastoreProto.WriteResult>(
                service, METHODID_APPEND_SINGLE_RESULT)))
        .build();
  }

  private static abstract class DataProcessingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    DataProcessingBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return datastore.DatastoreProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("DataProcessing");
    }
  }

  private static final class DataProcessingFileDescriptorSupplier
      extends DataProcessingBaseDescriptorSupplier {
    DataProcessingFileDescriptorSupplier() {}
  }

  private static final class DataProcessingMethodDescriptorSupplier
      extends DataProcessingBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    DataProcessingMethodDescriptorSupplier(java.lang.String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (DataProcessingGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new DataProcessingFileDescriptorSupplier())
              .addMethod(getReadMethod())
              .addMethod(getAppendSingleResultMethod())
              .build();
        }
      }
    }
    return result;
  }
}
