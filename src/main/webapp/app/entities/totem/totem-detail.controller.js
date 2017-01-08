(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TotemDetailController', TotemDetailController);

    TotemDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Totem', 'User'];

    function TotemDetailController($scope, $rootScope, $stateParams, previousState, entity, Totem, User) {
        var vm = this;

        vm.totem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('traveltotemApp:totemUpdate', function(event, result) {
            vm.totem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
