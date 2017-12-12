WSE = {
    id: new Date().getTime(),
    listeners: [],
    listernerId: 0,
    url: 'http://vps131654.ovh.net:8080',
    socket: null,
    socketId: -1,
    connected: false,
    session: false,
    sessions: [],

    connect: function(url) {
        // Check URL
        wse.url = url;
        wse.socket = io.connect(wse.url); //, {secure: true});

        wse.socket.on('connect', function() {
            wse.connected = true;
            wse.connectionCallback();
        });

        wse.socket.on('socket-id', function(socketId) {
            wse.socketId = socketId;
            wse.socketInitializedCallback();
        });

        wse.socket.on('msg', wse.receiveMessage);

        wse.socket.on('connect_error', function(error) {
            wse.connected = false;
            wse.connectionErrorCallback(error);
        });
        wse.socket.on('connect_timeout', function() {
            wse.connected = false;
            wse.connectionTimeoutCallback();
        });

        wse.socket.on('reconnect', function(n) {
            wse.connected = true;

            wse.reconnectionCallback(n);
            if (wse.session)
                wse.joinSession(wse.sessionName);
        });

        wse.socket.on('reconnect_error', function(error) {
            wse.connected = false;
            wse.reconnectionErrorCallback(error);
        });

        wse.socket.on('reconnect_failed', function() {
            wse.connected = false;
            wse.reconnectionFailedCallback();
        });

        wse.socket.on('reconnecting', function(n) {
            wse.reconnectingCallback(n);
        });

        wse.socket.on('error', function(error) {
            wse.errorCallback(error);
        });

        wse.socket.on('disconnect', function() {
            wse.connected = false;
            wse.disconnectedCallback();
        });
    },

    disconnect: function() {
        wse.socket.close();
        wse.disconnectionCallback();
    },

    joinSession: function(sessionName) {
        if (wse.connected) {
            wse.socket.emit('joinSession', sessionName);
            wse.session = true;
            wse.sessions.push(sessionName);
            wse.joinSessionCallback(sessionName);
        }
    },

    sendMessage: function(data, session) {
        if (session == undefined) session = wse.sessions[0];

        var o = {
            session: session,
            data: data
        };

        if (wse.connected && wse.session)
            wse.socket.emit('sendMsg', o);
    },

    receiveMessage: function(data) {
        wse.listeners.forEach(function(l) {
            if (l.session == data.session)
                l.onMessage(data);
        });
    },

    addListener: function(listener) {
        listener.id = wse.listernerId++;
        wse.listeners.push(listener);

        return listener;
    },

    removeListenerId: function(id) {
        wse.listeners = wse.listeners.filter(function(l) {
            return l.id != id;
        });
    },

    removeListener: function(listener) {
        wse.removeListenerId(listener.id);
    },

    /* Callbacks */
    connectionCallback: function() {
        console.log("connectionCallback");
    },
    connectionErrorCallback: function(error) {
        console.log('Connection Error : ' + error);
    },
    connectionTimeoutCallback: function() {
        console.log("connectionTimeoutCallback");
    },
    reconnectionCallback: function(n) {
        console.log("reconnectionCallback");
    },
    reconnectingCallback: function(n) {
        console.log("reconnectingCallback");
    },
    reconnectionErrorCallback: function(error) {
        console.log('Reconnection Error : ' + error);
    },
    reconnectionFailedCallback: function() {
        console.log("reconnectionFailedCallback");
    },
    disconnectionCallback: function() {
        console.log("disconnectionCallback");
    },
    disconnectedCallback: function() {
        console.log("disconnectedCallback");
    },
    errorCallback: function(error) {
        console.log('Error : ' + error);
    },
    joinSessionCallback: function() {
        console.log("joinSessionCallback");
    },
    socketInitializedCallback: function() {
        console.log("socketInitialized");
    },
}