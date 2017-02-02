(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TotemDialogController', TotemDialogController);

    TotemDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Totem', 'User'];

    function TotemDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Totem, User) {
        var vm = this;

        vm.totem = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.totem.id !== null) {
                Totem.update(vm.totem, onSaveSuccess, onSaveError);
            } else {
                Totem.save(vm.totem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('traveltotemApp:totemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.creationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
