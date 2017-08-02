package common;

import android.content.Intent;

public interface ActivityResult {
    public void onActivityResult(int requestCode, int resultCode, Intent data);
}