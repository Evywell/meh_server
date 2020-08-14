package fr.evywell.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ClientResponseFuture implements Future<ByteBuf> {
    private volatile State state = State.WAITING;
    ArrayBlockingQueue<ByteBuf> blockingResponse = new ArrayBlockingQueue<ByteBuf>(1);

    private enum State {
        WAITING,
        DONE
    }

    @Override
    public boolean isCancelled() {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Should return true when:
     *  - Future completed normally
     *  - Future generated an exception
     *  - Future got cancelled
     *
     */
    @Override
    public boolean isDone() {
        return state == State.DONE;
    }

    /**
     * Retrieves the response, blocking if necessary.
     *
     * As ArrayBlockingQueue is the structure being used to hold the response,
     * this adaptation puts the response back in the queue.. so that future
     * calls to get() will return the same value.
     *
     */
    @Override
    public ByteBuf get() throws InterruptedException, ExecutionException {
        ByteBuf aux = blockingResponse.take();
        blockingResponse.put(aux);
        return aux;
    }

    @Override
    public ByteBuf get(long timeout, TimeUnit unit) throws InterruptedException,
            ExecutionException, TimeoutException {
        final ByteBuf responseAfterWait = blockingResponse.poll(timeout, unit);
        if (responseAfterWait == null) {
            throw new TimeoutException();
        }
        return responseAfterWait;
    }

    @Override
    public boolean isSuccess() {
        return state == State.DONE;
    }

    @Override
    public boolean isCancellable() {
        return false;
    }

    @Override
    public Throwable cause() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> addListener(
            GenericFutureListener<? extends Future<? super ByteBuf>> listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> addListeners(
            GenericFutureListener<? extends Future<? super ByteBuf>>... listeners) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> removeListener(
            GenericFutureListener<? extends Future<? super ByteBuf>> listener) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> removeListeners(
            GenericFutureListener<? extends Future<? super ByteBuf>>... listeners) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> sync() throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> syncUninterruptibly() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> await() throws InterruptedException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Future<ByteBuf> awaitUninterruptibly() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean await(long timeout, TimeUnit unit)
            throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean await(long timeoutMillis) throws InterruptedException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long timeout, TimeUnit unit) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean awaitUninterruptibly(long timeoutMillis) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public ByteBuf getNow() {
        return blockingResponse.poll();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    /**
     * This method is meant to be used by the service that provides the
     * asynchronous answer.
     *
     * This method will be called only once.
     *
     * @param msg
     */
    public void set(ByteBuf msg) {
        if (state == State.DONE) {
            return;
        }

        try {
            blockingResponse.put(msg);
            state = State.DONE;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
