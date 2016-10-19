var appModule = angular.module('myApp', []);

appModule.controller('MainCtrl', ['socketService', '$scope', '$interval', function(socketService, $scope, $interval) {
    $scope.title = 'Welcome to the Twitter Sentiment Analysis Demo!';

    $scope.connected = false;
    $scope.tweets = [];
    $scope.neutral = 0;
    $scope.positive = 0;
    $scope.negative = 0;

    $scope.sum = function() {
        return $scope.neutral + $scope.positive + $scope.negative;
    }

    socketService.addListener(function(tweet) {
        $scope.connected = true;

        if(!("text" in tweet) || tweet.text === 'Connected') {
            return;
        }

        if(tweet.score < 0) {
            tweet.sentiment = 'negative';
            $scope.negative++;
        }
        else if(tweet.score > 0) {
            tweet.sentiment = 'positive';
            $scope.positive++;
        }
        else {
            tweet.sentiment = 'neutral';
            $scope.neutral++;
        }

        $scope.tweets.push(tweet);

        if($scope.tweets.length > 100) {
            $scope.tweets.splice(0, $scope.tweets.length - 100);
        }

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