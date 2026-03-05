package com.example.praktika.ui.profile

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.praktika.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class UserProfile(
    var firstName: String = "Emmanuel",
    var lastName: String = "Oyiboke",
    var address: String = "Nigeria",
    var phone: String = "1-5",
    var photoPath: String? = null
)

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    photoBitmap: Bitmap?,
    onProfileUpdate: (UserProfile, Bitmap?) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Локальные копии для редактирования
    var localProfile by remember { mutableStateOf(userProfile) }
    var localPhoto by remember { mutableStateOf(photoBitmap) }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Загрузка данных (только первый раз)
    LaunchedEffect(Unit) {
        isLoading = true
        delay(1500)
        val loadedProfile = UserProfile(
            firstName = "Emmanuel",
            lastName = "Oyiboke",
            address = "Nigeria",
            phone = "1-5",
            photoPath = userProfile.photoPath // сохраняем путь
        )
        localProfile = loadedProfile

        // Загружаем фото, если есть путь
        loadedProfile.photoPath?.let { path ->
            val file = File(path)
            if (file.exists()) {
                localPhoto = BitmapFactory.decodeFile(path)
            }
        }

        isLoading = false
    }

    // Сохранение фото
    fun savePhotoToFile(bitmap: Bitmap): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "IMG_$timeStamp.jpg"
        val file = File(context.filesDir, fileName)
        FileOutputStream(file).use { out ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
        }
        return file.absolutePath
    }

    // Камера
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageBitmap = result.data?.extras?.get("data") as? Bitmap
            imageBitmap?.let { bitmap ->
                localPhoto = bitmap
                val savedPath = savePhotoToFile(bitmap)
                localProfile = localProfile.copy(photoPath = savedPath)
                // 📌 Отправляем наверх
                onProfileUpdate(localProfile, bitmap)
            }
        }
    }

    // Отправляем изменения наверх при сохранении
    fun saveChanges() {
        onProfileUpdate(localProfile, localPhoto)
        isEditing = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        ProfileHeader(
            isEditing = isEditing,
            onEditClick = { isEditing = !isEditing },
            onSaveClick = {
                coroutineScope.launch {
                    isLoading = true
                    delay(1500)
                    saveChanges()
                    isLoading = false
                }
            }
        )

        if (isLoading) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            ProfileContent(
                userProfile = localProfile,
                isEditing = isEditing,
                photoBitmap = localPhoto,
                onAvatarClick = {
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    cameraLauncher.launch(intent)
                },
                onFirstNameChange = { localProfile = localProfile.copy(firstName = it) },
                onLastNameChange = { localProfile = localProfile.copy(lastName = it) },
                onAddressChange = { localProfile = localProfile.copy(address = it) },
                onPhoneChange = { localProfile = localProfile.copy(phone = it) }
            )
        }
    }
}

@Composable
fun ProfileHeader(
    isEditing: Boolean,
    onEditClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Профиль",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        if (isEditing) {
            Text(
                text = "Сохранить",
                fontSize = 16.sp,
                color = Color(0xFF1976D2),
                modifier = Modifier.clickable { onSaveClick() }
            )
        } else {
            Icon(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = "Редактировать",
                tint = Color(0xFF48B2E7),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onEditClick() }
            )
        }
    }
}

@Composable
fun ProfileContent(
    userProfile: UserProfile,
    isEditing: Boolean,
    photoBitmap: Bitmap?,
    onAvatarClick: () -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onAddressChange: (String) -> Unit,
    onPhoneChange: (String) -> Unit
) {
    var firstName by remember { mutableStateOf(userProfile.firstName) }
    var lastName by remember { mutableStateOf(userProfile.lastName) }
    var address by remember { mutableStateOf(userProfile.address) }
    var phone by remember { mutableStateOf(userProfile.phone) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        // Аватар
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFE0E0E0))
                .clickable { if (isEditing) onAvatarClick() }
        ) {
            if (photoBitmap != null) {
                Image(
                    bitmap = photoBitmap.asImageBitmap(),
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "Аватар",
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    tint = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "${userProfile.firstName} ${userProfile.lastName}",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (isEditing) {
            Text(
                text = "Изменить фото профиля",
                fontSize = 14.sp,
                color = Color(0xFF1976D2),
                modifier = Modifier.clickable { onAvatarClick() }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painter = painterResource(id = R.drawable.barcode),
            contentDescription = "Штрихкод",
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(horizontal = 32.dp)
        )

        // Поля профиля
        if (isEditing) {
            OutlinedTextField(
                value = firstName,
                onValueChange = {
                    firstName = it
                    onFirstNameChange(it)
                },
                label = { Text("Имя") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = lastName,
                onValueChange = {
                    lastName = it
                    onLastNameChange(it)
                },
                label = { Text("Фамилия") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = address,
                onValueChange = {
                    address = it
                    onAddressChange(it)
                },
                label = { Text("Адрес") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = {
                    phone = it
                    onPhoneChange(it)
                },
                label = { Text("Телефон") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF1976D2),
                    focusedLabelColor = Color(0xFF1976D2)
                )
            )
        } else {
            ProfileField(label = "Имя", value = userProfile.firstName)
            ProfileField(label = "Фамилия", value = userProfile.lastName)
            ProfileField(label = "Адрес", value = userProfile.address)
            ProfileField(label = "Телефон", value = userProfile.phone)
        }
    }
}

@Composable
fun ProfileField(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(4.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.small)
                .padding(16.dp)
        ) {
            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}