package projectannotations;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 * <pre>
 * The Network API service definition.
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.58.0)",
    comments = "Source: network_api.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class NetworkAPIGrpc {

  private NetworkAPIGrpc() {}

  public static final java.lang.String SERVICE_NAME = "projectannotations.NetworkAPI";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<projectannotations.RequestMessage,
      projectannotations.ResponseMessage> getProcessRequestMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "ProcessRequest",
      requestType = projectannotations.RequestMessage.class,
      responseType = projectannotations.ResponseMessage.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<projectannotations.RequestMessage,
      projectannotations.ResponseMessage> getProcessRequestMethod() {
    io.grpc.MethodDescriptor<projectannotations.RequestMessage, projectannotations.ResponseMessage> getProcessRequestMethod;
    if ((getProcessRequestMethod = NetworkAPIGrpc.getProcessRequestMethod) == null) {
      synchronized (NetworkAPIGrpc.class) {
        if ((getProcessRequestMethod = NetworkAPIGrpc.getProcessRequestMethod) == null) {
          NetworkAPIGrpc.getProcessRequestMethod = getProcessRequestMethod =
              io.grpc.MethodDescriptor.<projectannotations.RequestMessage, projectannotations.ResponseMessage>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "ProcessRequest"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  projectannotations.RequestMessage.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.lite.ProtoLiteUtils.marshaller(
                  projectannotations.ResponseMessage.getDefaultInstance()))
              .build();
        }
      }
    }
    return getProcessRequestMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static NetworkAPIStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NetworkAPIStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NetworkAPIStub>() {
        @java.lang.Override
        public NetworkAPIStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NetworkAPIStub(channel, callOptions);
        }
      };
    return NetworkAPIStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static NetworkAPIBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NetworkAPIBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NetworkAPIBlockingStub>() {
        @java.lang.Override
        public NetworkAPIBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NetworkAPIBlockingStub(channel, callOptions);
        }
      };
    return NetworkAPIBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static NetworkAPIFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<NetworkAPIFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<NetworkAPIFutureStub>() {
        @java.lang.Override
        public NetworkAPIFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new NetworkAPIFutureStub(channel, callOptions);
        }
      };
    return NetworkAPIFutureStub.newStub(factory, channel);
  }

  /**
   * <pre>
   * The Network API service definition.
   * </pre>
   */
  public interface AsyncService {

    /**
     * <pre>
     * An example RPC method.
     * </pre>
     */
    default void processRequest(projectannotations.RequestMessage request,
        io.grpc.stub.StreamObserver<projectannotations.ResponseMessage> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getProcessRequestMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service NetworkAPI.
   * <pre>
   * The Network API service definition.
   * </pre>
   */
  public static abstract class NetworkAPIImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return NetworkAPIGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service NetworkAPI.
   * <pre>
   * The Network API service definition.
   * </pre>
   */
  public static final class NetworkAPIStub
      extends io.grpc.stub.AbstractAsyncStub<NetworkAPIStub> {
    private NetworkAPIStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetworkAPIStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NetworkAPIStub(channel, callOptions);
    }

    /**
     * <pre>
     * An example RPC method.
     * </pre>
     */
    public void processRequest(projectannotations.RequestMessage request,
        io.grpc.stub.StreamObserver<projectannotations.ResponseMessage> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getProcessRequestMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service NetworkAPI.
   * <pre>
   * The Network API service definition.
   * </pre>
   */
  public static final class NetworkAPIBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<NetworkAPIBlockingStub> {
    private NetworkAPIBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetworkAPIBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NetworkAPIBlockingStub(channel, callOptions);
    }

    /**
     * <pre>
     * An example RPC method.
     * </pre>
     */
    public projectannotations.ResponseMessage processRequest(projectannotations.RequestMessage request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getProcessRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service NetworkAPI.
   * <pre>
   * The Network API service definition.
   * </pre>
   */
  public static final class NetworkAPIFutureStub
      extends io.grpc.stub.AbstractFutureStub<NetworkAPIFutureStub> {
    private NetworkAPIFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected NetworkAPIFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new NetworkAPIFutureStub(channel, callOptions);
    }

    /**
     * <pre>
     * An example RPC method.
     * </pre>
     */
    public com.google.common.util.concurrent.ListenableFuture<projectannotations.ResponseMessage> processRequest(
        projectannotations.RequestMessage request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getProcessRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_PROCESS_REQUEST = 0;

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
        case METHODID_PROCESS_REQUEST:
          serviceImpl.processRequest((projectannotations.RequestMessage) request,
              (io.grpc.stub.StreamObserver<projectannotations.ResponseMessage>) responseObserver);
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
          getProcessRequestMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              projectannotations.RequestMessage,
              projectannotations.ResponseMessage>(
                service, METHODID_PROCESS_REQUEST)))
        .build();
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (NetworkAPIGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .addMethod(getProcessRequestMethod())
              .build();
        }
      }
    }
    return result;
  }
}
