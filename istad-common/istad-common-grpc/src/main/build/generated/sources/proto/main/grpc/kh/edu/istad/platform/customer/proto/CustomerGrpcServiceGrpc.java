package kh.edu.istad.platform.customer.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@io.grpc.stub.annotations.GrpcGenerated
public final class CustomerGrpcServiceGrpc {

  private CustomerGrpcServiceGrpc() {}

  public static final java.lang.String SERVICE_NAME = "kh.edu.istad.customer.v1.CustomerGrpcService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<kh.edu.istad.platform.customer.proto.CustomerRequest,
      kh.edu.istad.platform.customer.proto.CustomerResponse> getGetCustomerByIdMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCustomerById",
      requestType = kh.edu.istad.platform.customer.proto.CustomerRequest.class,
      responseType = kh.edu.istad.platform.customer.proto.CustomerResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<kh.edu.istad.platform.customer.proto.CustomerRequest,
      kh.edu.istad.platform.customer.proto.CustomerResponse> getGetCustomerByIdMethod() {
    io.grpc.MethodDescriptor<kh.edu.istad.platform.customer.proto.CustomerRequest, kh.edu.istad.platform.customer.proto.CustomerResponse> getGetCustomerByIdMethod;
    if ((getGetCustomerByIdMethod = CustomerGrpcServiceGrpc.getGetCustomerByIdMethod) == null) {
      synchronized (CustomerGrpcServiceGrpc.class) {
        if ((getGetCustomerByIdMethod = CustomerGrpcServiceGrpc.getGetCustomerByIdMethod) == null) {
          CustomerGrpcServiceGrpc.getGetCustomerByIdMethod = getGetCustomerByIdMethod =
              io.grpc.MethodDescriptor.<kh.edu.istad.platform.customer.proto.CustomerRequest, kh.edu.istad.platform.customer.proto.CustomerResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "GetCustomerById"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.platform.customer.proto.CustomerRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  kh.edu.istad.platform.customer.proto.CustomerResponse.getDefaultInstance()))
              .setSchemaDescriptor(new CustomerGrpcServiceMethodDescriptorSupplier("GetCustomerById"))
              .build();
        }
      }
    }
    return getGetCustomerByIdMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CustomerGrpcServiceStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceStub>() {
        @java.lang.Override
        public CustomerGrpcServiceStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerGrpcServiceStub(channel, callOptions);
        }
      };
    return CustomerGrpcServiceStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports all types of calls on the service
   */
  public static CustomerGrpcServiceBlockingV2Stub newBlockingV2Stub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceBlockingV2Stub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceBlockingV2Stub>() {
        @java.lang.Override
        public CustomerGrpcServiceBlockingV2Stub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerGrpcServiceBlockingV2Stub(channel, callOptions);
        }
      };
    return CustomerGrpcServiceBlockingV2Stub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CustomerGrpcServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceBlockingStub>() {
        @java.lang.Override
        public CustomerGrpcServiceBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerGrpcServiceBlockingStub(channel, callOptions);
        }
      };
    return CustomerGrpcServiceBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CustomerGrpcServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<CustomerGrpcServiceFutureStub>() {
        @java.lang.Override
        public CustomerGrpcServiceFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new CustomerGrpcServiceFutureStub(channel, callOptions);
        }
      };
    return CustomerGrpcServiceFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void getCustomerById(kh.edu.istad.platform.customer.proto.CustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.platform.customer.proto.CustomerResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getGetCustomerByIdMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service CustomerGrpcService.
   */
  public static abstract class CustomerGrpcServiceImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return CustomerGrpcServiceGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service CustomerGrpcService.
   */
  public static final class CustomerGrpcServiceStub
      extends io.grpc.stub.AbstractAsyncStub<CustomerGrpcServiceStub> {
    private CustomerGrpcServiceStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerGrpcServiceStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerGrpcServiceStub(channel, callOptions);
    }

    /**
     */
    public void getCustomerById(kh.edu.istad.platform.customer.proto.CustomerRequest request,
        io.grpc.stub.StreamObserver<kh.edu.istad.platform.customer.proto.CustomerResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getGetCustomerByIdMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service CustomerGrpcService.
   */
  public static final class CustomerGrpcServiceBlockingV2Stub
      extends io.grpc.stub.AbstractBlockingStub<CustomerGrpcServiceBlockingV2Stub> {
    private CustomerGrpcServiceBlockingV2Stub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerGrpcServiceBlockingV2Stub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerGrpcServiceBlockingV2Stub(channel, callOptions);
    }

    /**
     */
    public kh.edu.istad.platform.customer.proto.CustomerResponse getCustomerById(kh.edu.istad.platform.customer.proto.CustomerRequest request) throws io.grpc.StatusException {
      return io.grpc.stub.ClientCalls.blockingV2UnaryCall(
          getChannel(), getGetCustomerByIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do limited synchronous rpc calls to service CustomerGrpcService.
   */
  public static final class CustomerGrpcServiceBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<CustomerGrpcServiceBlockingStub> {
    private CustomerGrpcServiceBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerGrpcServiceBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerGrpcServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public kh.edu.istad.platform.customer.proto.CustomerResponse getCustomerById(kh.edu.istad.platform.customer.proto.CustomerRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getGetCustomerByIdMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service CustomerGrpcService.
   */
  public static final class CustomerGrpcServiceFutureStub
      extends io.grpc.stub.AbstractFutureStub<CustomerGrpcServiceFutureStub> {
    private CustomerGrpcServiceFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CustomerGrpcServiceFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new CustomerGrpcServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<kh.edu.istad.platform.customer.proto.CustomerResponse> getCustomerById(
        kh.edu.istad.platform.customer.proto.CustomerRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getGetCustomerByIdMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CUSTOMER_BY_ID = 0;

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
        case METHODID_GET_CUSTOMER_BY_ID:
          serviceImpl.getCustomerById((kh.edu.istad.platform.customer.proto.CustomerRequest) request,
              (io.grpc.stub.StreamObserver<kh.edu.istad.platform.customer.proto.CustomerResponse>) responseObserver);
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
          getGetCustomerByIdMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              kh.edu.istad.platform.customer.proto.CustomerRequest,
              kh.edu.istad.platform.customer.proto.CustomerResponse>(
                service, METHODID_GET_CUSTOMER_BY_ID)))
        .build();
  }

  private static abstract class CustomerGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CustomerGrpcServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return kh.edu.istad.platform.customer.proto.CustomerProto.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CustomerGrpcService");
    }
  }

  private static final class CustomerGrpcServiceFileDescriptorSupplier
      extends CustomerGrpcServiceBaseDescriptorSupplier {
    CustomerGrpcServiceFileDescriptorSupplier() {}
  }

  private static final class CustomerGrpcServiceMethodDescriptorSupplier
      extends CustomerGrpcServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final java.lang.String methodName;

    CustomerGrpcServiceMethodDescriptorSupplier(java.lang.String methodName) {
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
      synchronized (CustomerGrpcServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CustomerGrpcServiceFileDescriptorSupplier())
              .addMethod(getGetCustomerByIdMethod())
              .build();
        }
      }
    }
    return result;
  }
}
