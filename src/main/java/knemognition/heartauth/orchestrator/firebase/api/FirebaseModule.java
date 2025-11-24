package knemognition.heartauth.orchestrator.firebase.api;

public interface FirebaseModule {


    /**
     * Send a data message to a device.
     *
     * @param fcmToken device token (must be non-null/non-blank for FCM-based implementations)
     * @param data     payload to include in the push message
     * @throws FirebaseSendException
     *         if validation fails or the push provider reports an error
     */
    void sendChallengeMessage(String fcmToken, ChallengePushMessage data);

    void sendRefDataCollectMessage(String fcmToken, RefDataCollectPushMessage data);
}
