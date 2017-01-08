(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TransferDialogController', TransferDialogController);

    TransferDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Transfer', 'User', 'Totem'];

    function TransferDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Transfer, User, Totem) {
        var vm = this;

        vm.transfer = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.totems = Totem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transfer.id !== null) {
                Transfer.update(vm.transfer, onSaveSuccess, onSaveError);
            } else {
                Transfer.save(vm.transfer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('traveltotemApp:transferUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
