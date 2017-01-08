(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TransferDetailController', TransferDetailController);

    TransferDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Transfer', 'User', 'Totem'];

    function TransferDetailController($scope, $rootScope, $stateParams, previousState, entity, Transfer, User, Totem) {
        var vm = this;

        vm.transfer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('traveltotemApp:transferUpdate', function(event, result) {
            vm.transfer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
