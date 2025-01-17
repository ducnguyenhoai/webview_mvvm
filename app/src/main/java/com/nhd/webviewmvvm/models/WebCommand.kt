package com.nhd.webviewmvvm.models

enum class WebCommand(
    val command: String,   // Command used externally (e.g., API, URL)
    val newCommand: String // Method name used internally
) {
    SCAN_QR("scan-qr", "scanQR"),
    COPY_TO_CLIPBOARD("copy-clipboard", "copyToClipboard"),
    GET_FROM_CLIPBOARD("get-from-clipboard", "getFromClipboard"),
    CAPTURE_SCREEN("capture-screen", "captureScreen"),
    GET_QR_FROM_FILE("get-qr-from-file", "getQRFromFile"),
    GET_QR_FROM_IMAGE("get-qr-from-image", "getQRFromImage"),
    GET_QR_FROM_CAMERA("get-qr-from-camera", "getQRFromCamera"),
    GET_QR_FROM_SCREEN("get-qr-from-screen", "getQRFromScreen"),
    BACKUP_DATA("backup-data", "backupData"),
    TAKE_PICTURE("take-picture", "takePicture"),
    SELECT_IMAGE("select-image", "selectImage"),
    SET_STATUS_BIOMETRIC("set-status-biometric", "setStatusBiometric"),
    SET_STATUS_WATCH_CONFIRM("on-off-confirm-watch", "setStatusWatchConfirm"),
    GET_BACKUP_FILES("get-backup-files", "getBackupFiles"),
    SHARE("share", "share"),
    SHARE_ITEM("share-item", "shareItem"),
    UNZIP_FILE_RESTORE("unzip-file-restore", "unzipFileRestore"),
    RESTORE_DATA("restore-data", "restoreData"),
    OPEN_SERVER_SOCKET("open-server-socket", "openServerSocket"),
    CLOSE_SERVER_SOCKET("close-server-socket", "closeServerSocket"),
    SEND_FILE("send-file", "sendFile"),
    CONNECT_TO_SERVER_SOCKET("connect-to-server-socket", "connectToServerSocket"),
    SEND_MESSAGE_TO_SERVER("send-message-to-server", "sendMessageToServer"),
    CLOSE_CLIENT_SOCKET("close-client-socket", "closeClientSocket"),
    IMPORT_BY_FILE("import-by-file", "importByFile"),
    GET_FILE_ZIP("get-file-zip", "getFileZip"),
    GET_FILE("get-file", "getFile"),
    UNZIP_FILE("unzip-file", "unzipFile"),
    READ_ABI_STRING("read-abi-string", "readAbiString"),
    OPEN_D_APP("open-dapp", "openDApp"),
    CHECK_IS_ONLINE("check-is-online", "checkIsOnline"),
    GET_STATUS_CONNECTED("get-status-connected", "getStatusConnected"),
    SEND_SMS_BY_DEFAULT_APP("send-sms-by-default-app", "sendSmsByDefaultApp"),
    SEND_MAIL_BY_SELECTABLE_APP("send-mail-by-selectable-app", "sendMailBySelectableApp"),
    SEND_TEXT_TO_TELEGRAM("send-text-to-telegram", "sendTextToTelegram"),
    WATCH_APPROVE("watch-approve", "watchApprove"),
    SYNC_DATA_TO_WATCH("sync-data-to-watch", "syncDataToWatch"),
    SHARE_SECRET_TO_WATCH("share-secret-to-watch", "shareSecretToWatch"),
    GET_APP_INFO_FROM_URL("get-app-info-from-url", "getAppInfoFromUrl"),
    CHECKING_SIGN_APP("checking-sign-app", "checkingSignApp"),
    SEND_TRANSACTION("send-transaction", "sendTransaction"),
    EXECUTE_SMART_CONTRACT("execute-smart-contract", "executeSmartContract"),
    CHECK_D_APP_EXIST("check-d-app-exist", "checkDAppExist"),
    WRITE_TO_LOCAL_STORAGE("write-to-local-storage", "writeToLocalStorage"),
    SHARE_D_APP_TO_PUBLIC("share-d-app-to-public", "shareDAppToPublic"),
    CHECK_DEVICE_HAS_NOTCH("has-device-notch", "hasDeviceNotch"),
    VIBRATE("vibrate", "vibrate"),
    GET_BIOMETRIC_TYPE("get-biometric-type", "getBiometricType"),
    RELOAD_ALL("reload-all", "reloadAll"),
    GET_ALL_D_APP_NO_GROUP("get-all-d-app-no-group", "getAllDAppNoGroup"),
    SHOW_BOTTOM("show-bottom", "showBottom"),
    HIDE_BOTTOM("hide-bottom", "hideBottom"),
    SET_BOTTOM_CONTENT("bottom-content", "bottomContent"),
    HANDLE_EVENT("handle-event", "handleEvent"),
    TEL("tel", "tel"),
    SELECT_DATE("select-date", "selectDate"),
    DATE_SELECTED("date-selected", "dateSelected"),
    CLOSE_CALENDAR("close-calendar", "closeCalendar"),
    ON_CLICK("on-click", "onClick"),
    OPEN_URL("open-url", "openUrl")
}
