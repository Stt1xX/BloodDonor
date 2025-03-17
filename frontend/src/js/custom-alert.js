import { ref } from "vue";

export const visible = ref(false);
export const message = ref('');

export const closeAlert = () => {
    visible.value = false;
    message.value = '';
};

let timer = null;

export const showAlert = (mess) => {
    if (timer != null) {
        clearTimeout(timer);
    }

    visible.value = true;
    message.value = mess;

    requestAnimationFrame(() => {
        const progressBar = document.querySelector('.animate-progress');
        if (progressBar) {
            progressBar.style.animation = 'none';
            progressBar.offsetHeight; // Trigger reflow
            progressBar.style.animation = '';
        }
    });

    timer = setTimeout(() => {
        closeAlert();
    }, 5000);
};