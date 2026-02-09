// Show first frame of videos in clip cards on hover
document.addEventListener('DOMContentLoaded', () => {
    const cards = document.querySelectorAll('.clip-card');
    cards.forEach(card => {
        const video = card.querySelector('video');
        if (!video) return;

        card.addEventListener('mouseenter', () => {
            video.currentTime = 0;
            video.play().catch(() => {});
        });
        card.addEventListener('mouseleave', () => {
            video.pause();
            video.currentTime = 0;
        });
    });
});
