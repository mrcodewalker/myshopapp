import {ProvinceService} from "../services/province.service";
import {ActivatedRoute, Router} from "@angular/router";
import {Provinces} from "../models/provinces";
import {District} from "../models/district";
import {Commune} from "../models/commune";
import {Province} from "../models/province";


export class ProvinceApi {
  provinceData: Provinces[] = [];
  provinces: Province[] = [];
  districts: District[] = [];
  communes: Commune[] = [];
  constructor(
    private provinceService: ProvinceService,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.getProvinces();
    this.getDetailProvinces();
  }
  getProvinces(){
    this.provinceService.getProvinces().subscribe({
      next: (response: any[]) => {
        debugger;
        this.provinceData = response;
        debugger;
      },
      complete: () => {
        debugger;
      },
      error: (error: any) => {
        console.log("Error fetching data: ",error.error.message);
      }
    });
  }
  getDetailProvinces(){
    let setProvinces = new Set();
    let setDistricts = new Set();
    this.provinceData.forEach((response) => {
      if (!setProvinces.has(response.province_id)){
        let cloneProvince: Province = {
          province_name: response.province_name,
          province_id: response.province_id
        }
        this.provinces.push(cloneProvince);
        setProvinces.add(response.province_id);
      }
    });
    this.provinceData.forEach((response) => {
      if (!setDistricts.has(response.district_id)){
        let cloneDistrict: District = {
          district_name: response.district_name,
          district_id: response.district_id
        }
        this.districts.push(cloneDistrict);
        setProvinces.add(response.district_id);
      }
    });
    this.provinceData.forEach((response) => {
        let cloneCommune: Commune = {
          commune_id: response.commune_id,
          commune_name: response.commune_name
        }
        this.communes.push(cloneCommune);
        setProvinces.add(response.province_id);
    });
  }
}
