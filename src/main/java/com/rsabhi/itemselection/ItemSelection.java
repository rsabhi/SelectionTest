/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rsabhi.itemselection;

/**
 *
 * @author abhi
 */
class item {
	public item() {
		price = (int)(Math.random() * 20 + 1); // 1 - 20
		cost = (int)(Math.random() * 4 + 2); // 2 - 5
		rating = (int)(Math.random() * 5 + 1); // 1 - 5
	}

	public item(int price, int cost, int rating) {
		super();
		this.price = price;
		this.cost = cost;
		this.rating = rating;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	private int price;
	private int cost;
	private int rating;
}

public class ItemSelection {
	private static int GROUPS = 20;
	private static int ITEMS = 10;
	private static int V = 49; // keep cost below 50, means max cost is 49
	private item[][] items = new item[GROUPS][ITEMS];
    private int[] f = new int[V + 1];

    private int[][] dp = new int[GROUPS + 1][V + 1];

	public ItemSelection() {
		// Initialize items
		for (int i = 0; i < GROUPS; i++) {
			for (int j = 0; j < ITEMS; j++) {
				items[i][j] = new item();
			}
		}

		for (int i = 0; i <= V; i++) f[i] = 0;
		for (int j = 0; j <= V; j++) dp[0][j] = 0;
		for (int i = 0; i <= GROUPS; i++) dp[i][0] = 0;
	}

	private void run() {
		// line 70 - 79 only calculates optimal sum of rating but doesn't track path
		for (int i = 0; i < GROUPS; i++) {
			for (int v = V; v >= 0; v--) {
				for (int j = 0; j < ITEMS; j++) {
					int totalcost = items[i][j].getCost() + items[i][j].getPrice();
					if (v >= totalcost) {
						f[v] = Math.max(f[v], f[v - totalcost] + items[i][j].getRating());
					}
				}
			}
		}

		// code below calculates optimal sum of rating and tracks path.
		for (int i = 1; i <= GROUPS; i++) {
			for (int j = 1; j <= V; j++) {
				int localmax = 0;

				for (int k = 0; k < ITEMS; k++) {
					int localcost = items[i - 1][k].getCost() + items[i - 1][k].getPrice();
					if (j >= localcost) {
						localmax = Math.max(localmax, dp[i - 1][j - localcost] + items[i - 1][k].getRating());
					}
				}

				dp[i][j] = Math.max(localmax, dp[i - 1][j]);
			}
		}

		int i = GROUPS;
		int j = V;

		int totalcost = 0;
		int totalrating = 0;
                System.out.println("====***** Printing the Selected items  ====*****");
		while ((i > 0) & (j > 0)) {
			for (int k = 0; k < ITEMS; k++) {
				int cost = items[i - 1][k].getCost() + items[i - 1][k].getPrice();
				int rating = items[i - 1][k].getRating();

				if ((j - cost >= 0) && (dp[i][j] == dp[i - 1][j - cost] + rating)) { // find the item causes the transfer
					System.out.printf("Category[%d]: Item[%d], cost = %d, rating = %d\n", i + 1, k + 1, cost, rating);
					totalcost += cost;
					totalrating += rating;
					j -= cost;
					break;
				}
			}
			i--;
		}

		System.out.printf("\nTotal Cost = %d\nSum of  Ratings = %d\n", totalcost, totalrating);
                System.out.println("=======**********=======");
		if (f[V] != dp[GROUPS][V]) {
			System.out.println("------- error 1-------------");
		}

		if (dp[GROUPS][V] != totalrating) {
			System.out.println("------- error 2-------------");
		}
	}


	public static void main(String[] args) {
		ItemSelection testItemSelection = new ItemSelection();
		testItemSelection.run();
	}
}
