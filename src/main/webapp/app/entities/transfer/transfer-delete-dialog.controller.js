(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('TransferDeleteController',TransferDeleteController);

    TransferDeleteController.$inject = ['$uibModalInstance', 'entity', 'Transfer'];

    function TransferDeleteController($uibModalInstance, entity, Transfer) {
        var vm = this;

        vm.transfer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Transfer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
