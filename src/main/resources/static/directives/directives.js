var appModule = angular.module('myApp');

appModule.directive('tweet', [function() {
    return {
         restrict: 'E',
         templateUrl: 'directives/tweet.html',

         scope: {
            tweet:"="
         },
         controller: function($scope) {
         }
    };
}]);