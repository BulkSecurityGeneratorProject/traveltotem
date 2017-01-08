(function() {
    'use strict';

    angular
        .module('traveltotemApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('transfer', {
            parent: 'entity',
            url: '/transfer',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'traveltotemApp.transfer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer/transfers.html',
                    controller: 'TransferController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transfer');
                    $translatePartialLoader.addPart('transferStatus');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('transfer-detail', {
            parent: 'entity',
            url: '/transfer/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'traveltotemApp.transfer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/transfer/transfer-detail.html',
                    controller: 'TransferDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transfer');
                    $translatePartialLoader.addPart('transferStatus');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Transfer', function($stateParams, Transfer) {
                    return Transfer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'transfer',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('transfer-detail.edit', {
            parent: 'transfer-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer/transfer-dialog.html',
                    controller: 'TransferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transfer', function(Transfer) {
                            return Transfer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer.new', {
            parent: 'transfer',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer/transfer-dialog.html',
                    controller: 'TransferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                latitude: null,
                                longitude: null,
                                status: null,
                                date: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('transfer', null, { reload: 'transfer' });
                }, function() {
                    $state.go('transfer');
                });
            }]
        })
        .state('transfer.edit', {
            parent: 'transfer',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer/transfer-dialog.html',
                    controller: 'TransferDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Transfer', function(Transfer) {
                            return Transfer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer', null, { reload: 'transfer' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('transfer.delete', {
            parent: 'transfer',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/transfer/transfer-delete-dialog.html',
                    controller: 'TransferDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Transfer', function(Transfer) {
                            return Transfer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('transfer', null, { reload: 'transfer' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
