package com.marcus.frotacerta.data.contacts

import android.content.ContentResolver
import android.provider.ContactsContract
import com.marcus.frotacerta.domain.model.ContactModel

class ContactsDataSource(
    private val contentResolver: ContentResolver
) {

    fun getContacts(): List<ContactModel> {

        val contacts = mutableListOf<ContactModel>()

        val projection = arrayOf(
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER
        )

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            projection,
            null,
            null,
            "${ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME} ASC"
        )

        cursor?.use {

            val idColumn = it.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID
            )

            val nameColumn = it.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )

            val phoneColumn = it.getColumnIndexOrThrow(
                ContactsContract.CommonDataKinds.Phone.NUMBER
            )

            while (it.moveToNext()) {

                val id = it.getLong(idColumn)

                val name = it.getString(nameColumn) ?: "Sem nome"

                val phone = it.getString(phoneColumn) ?: ""

                if (phone.isNotBlank()) {
                    contacts.add(
                        ContactModel(
                            id = id,
                            name = name,
                            phone = phone
                        )
                    )
                }
            }
        }

        return contacts
    }
}
