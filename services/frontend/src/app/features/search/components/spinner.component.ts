import {Component, Input} from '@angular/core';


@Component({
  selector: 'app-spinner',
  template: `
    <div class="spinner-overlay">
      <div
        class="spinner"
        [style.width.px]="size"
        [style.height.px]="size"
        [style.borderWidth.px]="borderWidth"
        [style.borderTopColor]="color"
      ></div>
    </div>
  `,
  styles: [
    `
      .spinner-overlay {
        display: flex;
        justify-content: center;
        align-items: center;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-color: rgba(255, 255, 255, 0.8);
        z-index: 1000;
      }

      .spinner {
        border: 6px solid #f3f3f3;
        border-radius: 50%;
        animation: spin 1s linear infinite;
      }

      @keyframes spin {
        0% {
          transform: rotate(0deg);
        }
        100% {
          transform: rotate(360deg);
        }
      }
    `,
  ],
  standalone: true,
})
export class SpinnerComponent {
  @Input() size = 50; // Default size in pixels
  @Input() borderWidth = 6; // Default border width in pixels
  @Input() color = '#ff6600'; // Default spinner color
}

