import { Component } from '@angular/core';
import { VisionService } from '../services/vision.service';

@Component({
  selector: 'app-material-predictor',
  templateUrl: './material-predictor.component.html',
  styleUrls: ['./material-predictor.component.scss']
})
export class MaterialPredictorComponent {
  result:any=null; loading=false; preview:string|ArrayBuffer|null=null;

  constructor(private vision: VisionService) {}

  onFile(e: any) {
    const file = e.target.files?.[0];
    if (!file) return;

    const r = new FileReader();
    r.onload = () => this.preview = r.result;
    r.readAsDataURL(file);

    this.loading = true;
    this.vision.predict(file).subscribe({
      next: (res) => { this.result = res; this.loading = false; },
      error: (err) => { this.result = { error: 'Erreur de prédiction' }; this.loading = false; }
    });
  }
}
