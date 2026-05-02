##
```vue
<script setup>
  const count = ref(0);
  const increment = () => {
    count.value++; 
  };
</script>
<template>
  <div>
    <span>{{ count }}</span><br>
    <UButton @click="increment">증가버튼</UButton>
  </div>
</template>
```
```html
<div>
  <span id="counter-display">0</span><br>
  <button id="up-btn">증가버튼</button>
</div>
<script>
  let count = 0; 

  document.getElementById('up-btn').addEventListener('click', () => {
    count++;
    
    const display = document.getElementById('counter-display');
    display.innerText = count; 
  });
</script>
```
- 요소가 1개여도 코드량이 줄어듬.
- 요소가 2개3개...N개되면 이때 엄청큰활약을함.
