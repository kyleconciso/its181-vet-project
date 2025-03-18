package com.group2.client.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.group2.client.model.Dog
import coil.compose.AsyncImage

@Composable
fun DogCard(dog: Dog, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = MaterialTheme.shapes.medium // Use MaterialTheme shapes
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically // Vertically center content
        ) {
            AsyncImage(
                model = dog.imageUrl,
                contentDescription = "Dog Image",
                modifier = Modifier
                    .size(80.dp)
                    .padding(end = 16.dp)
                    .clip(MaterialTheme.shapes.small), // Clip the image
                contentScale = ContentScale.Crop // Crop the image to fill the bounds
            )
            Column {
                Text(text = dog.name, style = MaterialTheme.typography.headlineSmall)
                Text(text = dog.breed, style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}