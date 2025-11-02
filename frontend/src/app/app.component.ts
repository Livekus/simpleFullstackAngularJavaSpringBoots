import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';


@Component({
    selector: 'app-root',
    standalone: true,
    imports: [RouterOutlet],
    template: `<h2>APP SHELL</h2><router-outlet />`
})
export class AppComponent {}