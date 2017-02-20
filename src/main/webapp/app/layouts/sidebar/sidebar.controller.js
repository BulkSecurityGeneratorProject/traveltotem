(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .controller('SidebarController', SidebarController);

    SidebarController.$inject = ['$scope', '$state','Principal', 'LoginService'];

    function SidebarController ($scope, $state, Principal, LoginService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }


    }
})();
