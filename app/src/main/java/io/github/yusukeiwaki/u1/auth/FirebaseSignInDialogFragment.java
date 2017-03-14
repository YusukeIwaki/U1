package io.github.yusukeiwaki.u1.auth;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import io.github.yusukeiwaki.u1.AbstractDialogFragment;
import io.github.yusukeiwaki.u1.R;

public class FirebaseSignInDialogFragment extends AbstractDialogFragment {
  private static final int RC_SIGN_IN = 0x10;

  public static FirebaseSignInDialogFragment newInstance() {
    return new FirebaseSignInDialogFragment();
  }

  private GoogleApiClient googleApiClient;
  private GoogleApiClient.OnConnectionFailedListener connectionFailedListener = connectionResult -> {
    onFailure(String.format("%d: %s",
        connectionResult.getErrorCode(), connectionResult.getErrorMessage()));
  };

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(getString(R.string.default_web_client_id))
        .requestEmail()
        .build();

    googleApiClient = new GoogleApiClient.Builder(getContext())
        .addOnConnectionFailedListener(connectionFailedListener)
        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        .build();
  }

  @Override protected int getLayout() {
    return R.layout.dialog_signin;
  }

  @Override protected void onSetupDialog() {
    getDialog().findViewById(R.id.btn_signin_google).setOnClickListener(btn -> {
      Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
      startActivityForResult(signInIntent, RC_SIGN_IN);
      showOrHideProgress(true);
    });

    showOrHideProgress(false);
    setCancelable(false);
  }

  private void showOrHideProgress(boolean show) {
    Dialog dialog = getDialog();
    dialog.findViewById(R.id.progress).setVisibility(show ? View.VISIBLE : View.GONE);
    dialog.findViewById(R.id.btn_signin_google).setEnabled(!show);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
      if (result.isSuccess()) {
        // Google Sign In was successful, authenticate with Firebase
        GoogleSignInAccount account = result.getSignInAccount();
        firebaseAuthWithGoogle(account);
      } else {
        onFailure(String.format("%d: %s",
            result.getStatus().getStatusCode(), result.getStatus().getStatusMessage()));
      }
    }
  }

  private void onFailure(String message) {
    Toast.makeText(getContext(), "onFailure: " + message, Toast.LENGTH_SHORT).show();
    showOrHideProgress(false);
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
    AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
    FirebaseAuth.getInstance().signInWithCredential(credential)
        .addOnCompleteListener(task -> {
          if (task.isSuccessful()) {
            dismiss();
          } else {
            onFailure(task.getException().getMessage());
          }
        });
  }
}
