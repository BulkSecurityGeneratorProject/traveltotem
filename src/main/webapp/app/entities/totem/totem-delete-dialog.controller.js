(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TotemDeleteController',TotemDeleteController);

    TotemDeleteController.$inject = ['$uibModalInstance', 'entity', 'Totem'];

    function TotemDeleteController($uibModalInstance, entity, Totem) {
        var vm = this;

        vm.totem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Totem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
