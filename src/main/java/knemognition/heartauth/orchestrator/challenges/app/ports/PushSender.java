package knemognition.heartauth.orchestrator.challenges.app.ports;


import knemognition.heartauth.orchestrator.firebase.api.FirebaseSendException;
import knemognition.heartauth.orchestrator.firebase.api.ChallengePushMessage;

public interface PushSender {

    /**
     * Send a data message to a device.
     *
     * @param fcmToken device token (must be non-null/non-blank for FCM-based implementations)
     * @param data     payload to include in the push message
     * @throws FirebaseSendException
     *         if validation fails or the push provider reports an error
     */
    void sendData(String fcmToken, ChallengePushMessage data);
}
