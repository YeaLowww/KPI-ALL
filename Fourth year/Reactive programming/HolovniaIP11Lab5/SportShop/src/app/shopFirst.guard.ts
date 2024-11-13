import { Injectable } from "@angular/core";
import {
    ActivatedRouteSnapshot, RouterStateSnapshot,
    Router
} from "@angular/router";
import { ShopComponent } from "./shop/shop.component";
@Injectable()
export class ShopFirstGuard {
    private firstNavigation = true;
    constructor(private router: Router) { }
    canActivate(route: ActivatedRouteSnapshot,
        state: RouterStateSnapshot): boolean {
        if (this.firstNavigation) {
            this.firstNavigation = false;
            if (route.component != ShopComponent) {
                this.router.navigateByUrl("/");
                return false;
            }
        }
        return true;
    }
}