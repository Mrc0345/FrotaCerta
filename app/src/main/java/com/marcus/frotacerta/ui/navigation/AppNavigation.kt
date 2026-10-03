package com.marcus.frotacerta.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marcus.frotacerta.FrotaCertaApplication
import com.marcus.frotacerta.ui.contacts.ContactPickerScreen
import com.marcus.frotacerta.ui.dashboard.DashboardScreen
import com.marcus.frotacerta.ui.dashboard.DashboardViewModel
import com.marcus.frotacerta.ui.dashboard.DashboardViewModelFactory
import com.marcus.frotacerta.ui.rental.NewRentalScreen
import com.marcus.frotacerta.ui.rental.RentalViewModel
import com.marcus.frotacerta.ui.rental.RentalViewModelFactory
import com.marcus.frotacerta.ui.vehicle.VehicleFormScreen
import com.marcus.frotacerta.ui.vehicle.VehicleListScreen
import com.marcus.frotacerta.ui.vehicle.VehicleViewModel
import com.marcus.frotacerta.ui.vehicle.VehicleViewModelFactory

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    val context = LocalContext.current

    val application =
        context.applicationContext as FrotaCertaApplication

    val vehicleViewModel: VehicleViewModel = viewModel(
        factory = VehicleViewModelFactory(
            application.container.vehicleRepository
        )
    )

    val dashboardViewModel: DashboardViewModel = viewModel(
        factory = DashboardViewModelFactory(
            application.container.rentalRepository
        )
    )

    val rentalViewModel: RentalViewModel = viewModel(
        factory = RentalViewModelFactory(
            rentalRepository = application.container.rentalRepository,
            vehicleRepository = application.container.vehicleRepository
        )
    )

    NavHost(
        navController = navController,
        startDestination = Routes.DASHBOARD
    ) {

        composable(
            route = Routes.DASHBOARD
        ) {

            DashboardScreen(
                viewModel = dashboardViewModel,
                onOpenVehicles = {
                    navController.navigate(Routes.VEHICLES)
                },
                onNewRental = {
                    navController.navigate(Routes.NEW_RENTAL)
                }
            )
        }

        composable(
            route = Routes.VEHICLES
        ) {

            VehicleListScreen(
                viewModel = vehicleViewModel,
                onAddVehicle = {
                    navController.navigate(Routes.NEW_VEHICLE)
                }
            )
        }

        composable(
            route = Routes.NEW_VEHICLE
        ) {

            VehicleFormScreen(
                viewModel = vehicleViewModel,
                onVehicleSaved = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.NEW_RENTAL
        ) { backStackEntry ->

            val contactId =
                backStackEntry
                    .savedStateHandle
                    .getStateFlow(
                        "contact_id",
                        -1L
                    )
                    .collectAsStateWithLifecycle()

            val contactName =
                backStackEntry
                    .savedStateHandle
                    .getStateFlow(
                        "contact_name",
                        ""
                    )
                    .collectAsStateWithLifecycle()

            val contactPhone =
                backStackEntry
                    .savedStateHandle
                    .getStateFlow(
                        "contact_phone",
                        ""
                    )
                    .collectAsStateWithLifecycle()

            NewRentalScreen(
                viewModel = rentalViewModel,
                contactId = contactId.value,
                contactName = contactName.value,
                contactPhone = contactPhone.value,

                onSelectContact = {
                    navController.navigate(
                        Routes.CONTACT_PICKER
                    )
                },

                onRentalCreated = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.CONTACT_PICKER
        ) {

            ContactPickerScreen(
                onContactSelected = { contact ->

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contact_id",
                            contact.id
                        )

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contact_name",
                            contact.name
                        )

                    navController
                        .previousBackStackEntry
                        ?.savedStateHandle
                        ?.set(
                            "contact_phone",
                            contact.phone
                        )

                    navController.popBackStack()
                }
            )
        }
    }
}
