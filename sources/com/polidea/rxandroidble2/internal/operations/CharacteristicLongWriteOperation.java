package com.polidea.rxandroidble2.internal.operations;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.os.DeadObjectException;
import androidx.annotation.NonNull;
import bleshadow.javax.inject.Named;
import com.polidea.rxandroidble2.RxBleConnection;
import com.polidea.rxandroidble2.exceptions.BleDisconnectedException;
import com.polidea.rxandroidble2.exceptions.BleException;
import com.polidea.rxandroidble2.exceptions.BleGattCallbackTimeoutException;
import com.polidea.rxandroidble2.exceptions.BleGattCannotStartException;
import com.polidea.rxandroidble2.exceptions.BleGattCharacteristicException;
import com.polidea.rxandroidble2.exceptions.BleGattException;
import com.polidea.rxandroidble2.exceptions.BleGattOperationType;
import com.polidea.rxandroidble2.internal.QueueOperation;
import com.polidea.rxandroidble2.internal.connection.PayloadSizeLimitProvider;
import com.polidea.rxandroidble2.internal.connection.RxBleGattCallback;
import com.polidea.rxandroidble2.internal.serialization.QueueReleaseInterface;
import com.polidea.rxandroidble2.internal.util.ByteAssociation;
import com.polidea.rxandroidble2.internal.util.DisposableUtil;
import com.polidea.rxandroidble2.internal.util.QueueReleasingEmitterWrapper;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import java.nio.ByteBuffer;
import java.util.UUID;

/* loaded from: classes.dex */
public class CharacteristicLongWriteOperation extends QueueOperation<byte[]> {
    private final PayloadSizeLimitProvider batchSizeProvider;
    private final BluetoothGatt bluetoothGatt;
    private final BluetoothGattCharacteristic bluetoothGattCharacteristic;
    private final Scheduler bluetoothInteractionScheduler;
    private final byte[] bytesToWrite;
    private final RxBleGattCallback rxBleGattCallback;
    private byte[] tempBatchArray;
    private final TimeoutConfiguration timeoutConfiguration;
    private final RxBleConnection.WriteOperationAckStrategy writeOperationAckStrategy;
    private final RxBleConnection.WriteOperationRetryStrategy writeOperationRetryStrategy;

    /* JADX INFO: Access modifiers changed from: package-private */
    public CharacteristicLongWriteOperation(BluetoothGatt bluetoothGatt, RxBleGattCallback rxBleGattCallback, @Named("bluetooth_interaction") Scheduler scheduler, @Named("operation-timeout") TimeoutConfiguration timeoutConfiguration, BluetoothGattCharacteristic bluetoothGattCharacteristic, PayloadSizeLimitProvider payloadSizeLimitProvider, RxBleConnection.WriteOperationAckStrategy writeOperationAckStrategy, RxBleConnection.WriteOperationRetryStrategy writeOperationRetryStrategy, byte[] bArr) {
        this.bluetoothGatt = bluetoothGatt;
        this.rxBleGattCallback = rxBleGattCallback;
        this.bluetoothInteractionScheduler = scheduler;
        this.timeoutConfiguration = timeoutConfiguration;
        this.bluetoothGattCharacteristic = bluetoothGattCharacteristic;
        this.batchSizeProvider = payloadSizeLimitProvider;
        this.writeOperationAckStrategy = writeOperationAckStrategy;
        this.writeOperationRetryStrategy = writeOperationRetryStrategy;
        this.bytesToWrite = bArr;
    }

    @Override // com.polidea.rxandroidble2.internal.QueueOperation
    protected void protectedRun(ObservableEmitter<byte[]> observableEmitter, QueueReleaseInterface queueReleaseInterface) throws Throwable {
        int payloadSizeLimit = this.batchSizeProvider.getPayloadSizeLimit();
        if (payloadSizeLimit <= 0) {
            throw new IllegalArgumentException("batchSizeProvider value must be greater than zero (now: " + payloadSizeLimit + ")");
        }
        Observable error = Observable.error(new BleGattCallbackTimeoutException(this.bluetoothGatt, BleGattOperationType.CHARACTERISTIC_LONG_WRITE));
        ByteBuffer wrap = ByteBuffer.wrap(this.bytesToWrite);
        final QueueReleasingEmitterWrapper queueReleasingEmitterWrapper = new QueueReleasingEmitterWrapper(observableEmitter, queueReleaseInterface);
        writeBatchAndObserve(payloadSizeLimit, wrap).subscribeOn(this.bluetoothInteractionScheduler).filter(writeResponseForMatchingCharacteristic(this.bluetoothGattCharacteristic)).take(1L).timeout(this.timeoutConfiguration.timeout, this.timeoutConfiguration.timeoutTimeUnit, this.timeoutConfiguration.timeoutScheduler, error).repeatWhen(bufferIsNotEmptyAndOperationHasBeenAcknowledgedAndNotUnsubscribed(this.writeOperationAckStrategy, wrap, queueReleasingEmitterWrapper)).retryWhen(errorIsRetryableAndAccordingTo(this.writeOperationRetryStrategy, wrap, payloadSizeLimit)).subscribe(new Observer<ByteAssociation<UUID>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.1
            @Override // io.reactivex.Observer
            public void onNext(ByteAssociation<UUID> byteAssociation) {
            }

            @Override // io.reactivex.Observer
            public void onSubscribe(Disposable disposable) {
            }

            @Override // io.reactivex.Observer
            public void onError(Throwable th) {
                queueReleasingEmitterWrapper.onError(th);
            }

            @Override // io.reactivex.Observer
            public void onComplete() {
                queueReleasingEmitterWrapper.onNext(CharacteristicLongWriteOperation.this.bytesToWrite);
                queueReleasingEmitterWrapper.onComplete();
            }
        });
    }

    @Override // com.polidea.rxandroidble2.internal.QueueOperation
    protected BleException provideException(DeadObjectException deadObjectException) {
        return new BleDisconnectedException(deadObjectException, this.bluetoothGatt.getDevice().getAddress());
    }

    @NonNull
    private Observable<ByteAssociation<UUID>> writeBatchAndObserve(final int i, final ByteBuffer byteBuffer) {
        final Observable<ByteAssociation<UUID>> onCharacteristicWrite = this.rxBleGattCallback.getOnCharacteristicWrite();
        return Observable.create(new ObservableOnSubscribe<ByteAssociation<UUID>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.2
            @Override // io.reactivex.ObservableOnSubscribe
            public void subscribe(ObservableEmitter<ByteAssociation<UUID>> observableEmitter) throws Exception {
                observableEmitter.setDisposable((DisposableObserver) onCharacteristicWrite.subscribeWith(DisposableUtil.disposableObserverFromEmitter(observableEmitter)));
                try {
                    CharacteristicLongWriteOperation.this.writeData(CharacteristicLongWriteOperation.this.getNextBatch(byteBuffer, i));
                } catch (Throwable th) {
                    observableEmitter.onError(th);
                }
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public byte[] getNextBatch(ByteBuffer byteBuffer, int i) {
        int min = Math.min(byteBuffer.remaining(), i);
        if (this.tempBatchArray == null || this.tempBatchArray.length != min) {
            this.tempBatchArray = new byte[min];
        }
        byteBuffer.get(this.tempBatchArray);
        return this.tempBatchArray;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void writeData(byte[] bArr) {
        this.bluetoothGattCharacteristic.setValue(bArr);
        if (!this.bluetoothGatt.writeCharacteristic(this.bluetoothGattCharacteristic)) {
            throw new BleGattCannotStartException(this.bluetoothGatt, BleGattOperationType.CHARACTERISTIC_LONG_WRITE);
        }
    }

    private static Predicate<ByteAssociation<UUID>> writeResponseForMatchingCharacteristic(final BluetoothGattCharacteristic bluetoothGattCharacteristic) {
        return new Predicate<ByteAssociation<UUID>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.3
            @Override // io.reactivex.functions.Predicate
            public boolean test(ByteAssociation<UUID> byteAssociation) {
                return byteAssociation.first.equals(bluetoothGattCharacteristic.getUuid());
            }
        };
    }

    private static Function<Observable<?>, ObservableSource<?>> bufferIsNotEmptyAndOperationHasBeenAcknowledgedAndNotUnsubscribed(final RxBleConnection.WriteOperationAckStrategy writeOperationAckStrategy, final ByteBuffer byteBuffer, final QueueReleasingEmitterWrapper<byte[]> queueReleasingEmitterWrapper) {
        return new Function<Observable<?>, ObservableSource<?>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.4
            @Override // io.reactivex.functions.Function
            public ObservableSource<?> apply(Observable<?> observable) throws Exception {
                return observable.takeWhile(notUnsubscribed(QueueReleasingEmitterWrapper.this)).map(bufferIsNotEmpty(byteBuffer)).compose(writeOperationAckStrategy).takeUntil(new Predicate<Boolean>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.4.1
                    @Override // io.reactivex.functions.Predicate
                    public boolean test(Boolean bool) throws Exception {
                        return !bool.booleanValue();
                    }
                });
            }

            @NonNull
            private Function<Object, Boolean> bufferIsNotEmpty(final ByteBuffer byteBuffer2) {
                return new Function<Object, Boolean>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.4.2
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // io.reactivex.functions.Function
                    public Boolean apply(Object obj) throws Exception {
                        return Boolean.valueOf(byteBuffer2.hasRemaining());
                    }
                };
            }

            @NonNull
            private Predicate<Object> notUnsubscribed(final QueueReleasingEmitterWrapper<byte[]> queueReleasingEmitterWrapper2) {
                return new Predicate<Object>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.4.3
                    @Override // io.reactivex.functions.Predicate
                    public boolean test(Object obj) {
                        return !queueReleasingEmitterWrapper2.isWrappedEmitterUnsubscribed();
                    }
                };
            }
        };
    }

    private static Function<Observable<Throwable>, ObservableSource<?>> errorIsRetryableAndAccordingTo(final RxBleConnection.WriteOperationRetryStrategy writeOperationRetryStrategy, final ByteBuffer byteBuffer, final int i) {
        return new Function<Observable<Throwable>, ObservableSource<?>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.5
            @Override // io.reactivex.functions.Function
            public ObservableSource<?> apply(Observable<Throwable> observable) {
                return observable.flatMap(toLongWriteFailureOrError()).doOnNext(repositionByteBufferForRetry()).compose(RxBleConnection.WriteOperationRetryStrategy.this);
            }

            @NonNull
            private Function<Throwable, Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure>> toLongWriteFailureOrError() {
                return new Function<Throwable, Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure>>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.5.1
                    @Override // io.reactivex.functions.Function
                    public Observable<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure> apply(Throwable th) {
                        if (!(th instanceof BleGattCharacteristicException) && !(th instanceof BleGattCannotStartException)) {
                            return Observable.error(th);
                        }
                        return Observable.just(new RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure(calculateFailedBatchIndex(byteBuffer, i), (BleGattException) th));
                    }
                };
            }

            @NonNull
            private Consumer<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure> repositionByteBufferForRetry() {
                return new Consumer<RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure>() { // from class: com.polidea.rxandroidble2.internal.operations.CharacteristicLongWriteOperation.5.2
                    @Override // io.reactivex.functions.Consumer
                    public void accept(RxBleConnection.WriteOperationRetryStrategy.LongWriteFailure longWriteFailure) {
                        byteBuffer.position(longWriteFailure.getBatchIndex() * i);
                    }
                };
            }

            /* JADX INFO: Access modifiers changed from: private */
            public int calculateFailedBatchIndex(ByteBuffer byteBuffer2, int i2) {
                return ((int) Math.ceil(byteBuffer2.position() / i2)) - 1;
            }
        };
    }
}
