package com.deendayalproject.esop.dashboard






import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CardMembership
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.deendayalproject.R
import kotlin.String

@Composable
fun DashboardSection(
    navController: NavController,
    candidateLoginEmail: String,
    candidateLoginId: String,
    candidateName: String,
    candidateMobileNo: String
) {
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxWidth()
    )
    {
            DashboardCard(
                modifier = Modifier.fillMaxWidth(),
                title = stringResource(R.string.my_tests),
                subtitle = stringResource(R.string.view_your_tests),
                icon = Icons.Default.Assignment,
                onClick = {
                    val bundle = bundleOf(
                        "candidateLoginEmail" to candidateLoginEmail,
                        "candidateLoginId" to candidateLoginId
                    )
                    navController.navigate(R.id.action_esopFragment_to_esopTestFragment,bundle)

                },
                candidateName=candidateName,
                candidateMobileNo=candidateMobileNo
            )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.results),
            subtitle = stringResource(R.string.view_your_results),
            icon = Icons.Default.BarChart,
            onClick = {
                val bundle = bundleOf(
                    "candidateLoginEmail" to candidateLoginEmail,
                    "candidateLoginId" to candidateLoginId,
                    "candidateName" to candidateName,
                    "candidateMobileNo" to candidateMobileNo
                )

                navController.navigate(R.id.action_esopFragment_to_esopResultFragment,bundle)


            },
            candidateName=candidateName,
            candidateMobileNo=candidateMobileNo
        )

        Spacer(modifier = Modifier.height(12.dp))

        DashboardCard(
            modifier = Modifier.fillMaxWidth(),
            title = stringResource(R.string.certificate),
            subtitle = stringResource(R.string.view_download),
            icon = Icons.Default.CardMembership,
            onClick = {

                val bundle = bundleOf(

                    "candidateName" to candidateName,
                    "candidateMobileNo" to candidateMobileNo,
                )
                navController.navigate(R.id.action_esopFragment_to_esopgetCertificateFragment,bundle)

            },
            candidateName=candidateName,
            candidateMobileNo=candidateMobileNo
        )
    }

}
//      private fun navigate(id: Int) {
//    findNavController().navigate(id)
//      }
