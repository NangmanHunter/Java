## TemplateSlot
```.vue
<script setup lang="ts">
import type { TableColumn } from '@nuxt/ui'


type Test ={
  id: string
  isApproved: boolean
}




const data = ref<Test[]>([
  {
    id: 'true',
    isApproved: true
  },
  {
    id: '4600',
    isApproved: false
  }
])

const columns : TableColumn<Test>[] = [
    {
        accessorKey: 'id',
        header: '#',
        cell: ({ row }) => 
        `#${row.getValue('id')}`
    },
    {
        accessorKey: 'isApproved',
        header: '승인 여부',
        cell: ({ row }) => row.getValue('isApproved') ? '승인' : '미승인'
    }
]

</script>

<template>

  <UTable :data="data" :columns="columns">
    <!-- id 컬럼만 특별하게 그리고 싶을 때 -->
    <template #id-cell="{ row }">
       <div class="flex items-center gap-2">
         <!-- <span>#{{ row.getValue('id') }}</span> -->
         <UCheckbox v-model="row.original.isApproved" />
       </div>
    </template>
  </UTable>    

</template>
```