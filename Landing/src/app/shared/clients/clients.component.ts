import { Component } from '@angular/core';

export interface EnvironmentalImpactRange {
  material: string;
  gwp: [number, number]; // in kg CO2 eq/L
  water: [number, number]; // in m³/L
  energy: [number, number]; // in MJ/L
}

@Component({
  selector: 'app-clients',
  templateUrl: './clients.component.html',
  styleUrls: ['./clients.component.scss']
})
export class ClientsComponent {
  materials: EnvironmentalImpactRange[] = [
    { material: 'Bag-in-Box', gwp: [0.200124, 0.399930], water: [0.050411, 0.149774], energy: [10.007913, 19.998362] },
    { material: 'Brown Glass', gwp: [0.240263, 0.449984], water: [0.120333, 0.349400], energy: [3.608403, 5.998998] },
    { material: 'Ceramic Bottle/Jar', gwp: [3.006382, 0.499960], water: [0.100689, 0.299947], energy: [80.012731, 149.763963] },
    { material: 'Clear Glass', gwp: [0.320007, 0.599483], water: [0.200349, 0.498502], energy: [4.818052, 7.499133] },
    { material: 'Green Glass', gwp: [0.280885, 0.480422], water: [0.161411, 0.399031], energy: [1.608189, 6.492690] },
    { material: 'HDPE', gwp: [0.024056, 0.056944], water: [0.202033, 0.598042], energy: [1.408662, 2.699239] },
    { material: 'LDPE', gwp: [0.026026, 0.065944], water: [0.301892, 0.748253], energy: [1.608189, 2.995453] },
    { material: 'PE/PET/Aluminum Foil', gwp: [0.050239, 0.149982], water: [0.010113, 0.029976], energy: [20.052044, 39.942997] },
    { material: 'PET', gwp: [0.036056, 0.059760], water: [0.500215, 0.998831], energy: [2.251324, 3.391033] },
    { material: 'PLA/bio-PE', gwp: [0.030180, 0.079831], water: [0.020035, 0.049887], energy: [10.044478, 24.998959] },
    { material: 'Recycled Glass', gwp: [0.121169, 0.299545], water: [0.040520, 0.148965], energy: [1.605286, 3.995545] },
    { material: 'Tetra Pak', gwp: [0.100019, 0.199983], water: [0.050009, 0.099950], energy: [15.003388, 29.958135] }
  ];


  selectedMaterial?: EnvironmentalImpactRange;
  volumeLiters: number = 1;

  getImpactRange() {
    if (!this.selectedMaterial) return null;

    const { gwp, water, energy } = this.selectedMaterial;

    return {
      gwp: [gwp[0] * this.volumeLiters, gwp[1] * this.volumeLiters],
      water: [water[0] * this.volumeLiters, water[1] * this.volumeLiters],
      energy: [energy[0] * this.volumeLiters, energy[1] * this.volumeLiters]
    };
  }
}
