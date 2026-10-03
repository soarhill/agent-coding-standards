<template>
  <div>
    <div
      v-for="(file, index) in files"
      :key="index"
      class="row"
    >
      <span>{{ file.fileName }}</span>
      <span>{{ tableStatusText(file.status) }}</span>
      <input v-model="file.note" />
      <button @click="openEdit(file)">Edit</button>
    </div>

    <div v-if="editingFile">
      <h3>Edit {{ editingFile.fileName }}</h3>
      <p>Status: {{ editStatusText(editingFile.status) }}</p>
      <button @click="editingFile = null">Close</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const files = ref([
  { id: 10, fileName: 'guide.pdf', status: 0, note: '' },
  { id: 11, fileName: 'faq.md', status: 1, note: '' },
  { id: 12, fileName: 'api.txt', status: 2, note: '' },
  { id: 13, fileName: 'ops.md', status: 3, note: '' },
  { id: 14, fileName: 'broken.txt', status: 4, note: '' },
])

const editingFile = ref(null)

function tableStatusText(status) {
  if (status === 0) return 'Uploading'
  if (status === 1) return 'Pending'
  if (status === 2) return 'Vectorizing'
  if (status === 3) return 'Completed'
  if (status === 4) return 'Failed'
  return 'Unknown'
}

function editStatusText(status) {
  if (status === 0) return 'Pending'
  if (status === 1) return 'Vectorizing'
  if (status === 2) return 'Completed'
  if (status === 3) return 'Failed'
  return 'Unknown'
}

function openEdit(file) {
  editingFile.value = file
}
</script>
