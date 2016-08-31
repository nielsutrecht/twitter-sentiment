var appModule = angular.module('myApp', []);

appModule.controller('MainCtrl', ['socketService', '$scope', '$interval', function(socketService, $scope, $interval) {
    $scope.title = 'Welcome to the Twitter Sentiment Analysis Demo!';

    $scope.connected = false;
    $scope.tweets = [];

    socketService.addListener(function(tweet) {
        $scope.tweets.push(tweet);
        $scope.connected = true;

        $scope.$apply();
    });

    socketService.connect();
}]);

appModule.service('socketService', ['$rootScope', function($rootScope) {
    var listeners = [];
    var stompClient;
    return {
        connect: function() {
            var self = this;
            var socket = new SockJS('/socket');
            stompClient = Stomp.over(socket);
            stompClient.debug = null;
            stompClient.connect({}, function(frame) {
                self.notifyListeners({'status':'connected'});
                stompClient.subscribe('/topic/status', function(message) {
                    console.log(message.body);
                    self.notifyListeners(JSON.parse(message.body));
                });

                stompClient.send("/app/status", {}, JSON.stringify({}));
            },
            function(error) {
                self.notifyListeners({'status':'error'});
            });
        },

        addListener: function(callback) {
            listeners.push(callback);
        },

        notifyListeners: function(status) {
            angular.forEach(listeners, function(cb) {
                cb(status);
            });
        },

        disconnect: function() {
            if (stompClient != null) {
                stompClient.disconnect();
            }
            console.log("Disconnected");
        }
    };
}]);