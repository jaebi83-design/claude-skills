package com.inventoryshopping.meijer.domain.matcher

import javax.inject.Inject

class CategoryInferrer @Inject constructor() {

    private val categoryRules: List<Pair<String, (String) -> Boolean>> = listOf(
        // Produce
        "Produce" to { name ->
            name.matchesAny(
                "apple", "banana", "orange", "grape", "strawberry", "blueberry", "raspberry",
                "lemon", "lime", "avocado", "tomato", "potato", "onion", "garlic", "ginger",
                "carrot", "celery", "broccoli", "cauliflower", "lettuce", "spinach", "kale",
                "pepper", "cucumber", "zucchini", "squash", "corn on the cob", "mushroom",
                "cilantro", "parsley", "basil", "mint", "dill", "herbs", "salad", "coleslaw",
                "fruit", "berries", "melon", "watermelon", "cantaloupe", "pineapple", "mango",
                "peach", "pear", "plum", "nectarine", "asparagus", "green bean", "cabbage",
                "radish", "beet", "turnip", "sweet potato", "yam"
            )
        },
        // Dairy
        "Dairy" to { name ->
            name.matchesAny(
                "milk", "cheese", "yogurt", "butter", "cream cheese", "sour cream",
                "cottage cheese", "eggs", "creamer", "whipped cream", "half and half",
                "margarine", "cream"
            )
        },
        // Meat & Seafood
        "Meat & Seafood" to { name ->
            name.matchesAny(
                "chicken", "beef", "pork", "steak", "ground beef", "ground turkey",
                "turkey", "sausage", "bacon", "ham", "lamb", "salmon", "shrimp", "tilapia",
                "cod", "tuna steak", "crab", "lobster", "fish", "ribs", "roast", "brisket",
                "hot dog", "bratwurst"
            )
        },
        // Frozen Foods
        "Frozen Foods" to { name ->
            name.matchesAny(
                "frozen", "ice cream", "pizza rolls", "frozen pizza", "frozen vegetable",
                "frozen fruit", "popsicle", "frozen dinner", "frozen meal", "fish sticks",
                "frozen waffle", "frozen fries", "tater tots", "frozen pie"
            )
        },
        // Bakery
        "Bakery" to { name ->
            name.matchesAny(
                "bread", "rolls", "bagel", "muffin", "croissant", "donut", "doughnut",
                "cake", "pie", "pastry", "tortilla", "pita", "bun", "english muffin",
                "baguette", "ciabatta", "sourdough"
            )
        },
        // Deli
        "Deli" to { name ->
            name.matchesAny(
                "deli meat", "deli cheese", "rotisserie", "lunch meat", "salami",
                "prosciutto", "prepared food", "sub sandwich", "hummus"
            )
        },
        // Cereal & Breakfast
        "Cereal & Breakfast" to { name ->
            name.matchesAny(
                "cereal", "oatmeal", "granola", "cheerios", "frosted flakes",
                "raisin bran", "corn flakes", "pop-tarts", "poptarts", "breakfast bar",
                "pancake mix", "waffle mix"
            )
        },
        // Snacks
        "Snacks" to { name ->
            name.matchesAny(
                "chip", "chips", "crackers", "popcorn", "pretzel", "nuts", "trail mix",
                "goldfish", "cheez-it", "doritos", "cheetos", "tortilla chips", "salsa",
                "guacamole", "snack"
            )
        },
        // Condiments
        "Condiments" to { name ->
            name.matchesAny(
                "ketchup", "mustard", "mayonnaise", "mayo", "bbq sauce", "hot sauce",
                "soy sauce", "worcestershire", "salad dressing", "ranch", "vinegar",
                "olive oil", "vegetable oil", "cooking spray", "relish", "pickles"
            )
        },
        // Canned Goods
        "Canned Goods" to { name ->
            name.matchesAny(
                "canned", "soup", "broth", "stock", "tuna", "canned beans", "chili",
                "tomato sauce", "tomato paste", "diced tomatoes", "canned corn",
                "canned fruit", "spam"
            )
        },
        // Baking
        "Baking" to { name ->
            name.matchesAny(
                "flour", "sugar", "baking soda", "baking powder", "vanilla extract",
                "chocolate chips", "cocoa", "yeast", "powdered sugar", "brown sugar",
                "sprinkles", "frosting", "cake mix", "brownie mix", "cornstarch",
                "food coloring"
            )
        },
        // Spices
        "Spices" to { name ->
            name.matchesAny(
                "salt", "pepper", "cinnamon", "paprika", "cumin", "oregano", "thyme",
                "rosemary", "chili powder", "garlic powder", "onion powder", "nutmeg",
                "cayenne", "turmeric", "bay leaves", "seasoning", "spice"
            )
        },
        // Beverages
        "Beverages" to { name ->
            name.matchesAny(
                "soda", "pop", "coke", "pepsi", "sprite", "ginger ale", "juice",
                "orange juice", "apple juice", "water", "sparkling water", "gatorade",
                "energy drink", "lemonade", "iced tea", "kombucha"
            )
        },
        // Coffee & Tea
        "Coffee & Tea" to { name ->
            name.matchesAny(
                "coffee", "tea", "k-cup", "kcup", "coffee beans", "ground coffee",
                "instant coffee", "green tea", "herbal tea", "creamer"
            )
        },
        // Pasta & Rice
        "Pasta & Rice" to { name ->
            name.matchesAny(
                "pasta", "spaghetti", "penne", "macaroni", "noodle", "rice",
                "pasta sauce", "marinara", "alfredo", "ramen", "mac and cheese",
                "lasagna", "rice-a-roni"
            )
        },
        // Cleaning
        "Cleaning" to { name ->
            name.matchesAny(
                "soap", "detergent", "bleach", "cleaner", "wipes", "sponge",
                "dish soap", "laundry", "fabric softener", "dryer sheets", "lysol",
                "clorox", "mop", "broom", "trash bag", "garbage bag"
            )
        },
        // Paper Products
        "Paper Products" to { name ->
            name.matchesAny(
                "paper towel", "toilet paper", "napkin", "tissue", "paper plate",
                "plastic wrap", "aluminum foil", "ziploc", "storage bag", "parchment"
            )
        },
        // Health & Beauty
        "Health & Beauty" to { name ->
            name.matchesAny(
                "shampoo", "conditioner", "body wash", "soap bar", "deodorant",
                "toothpaste", "toothbrush", "mouthwash", "floss", "lotion",
                "sunscreen", "band-aid", "bandage", "medicine", "vitamin",
                "ibuprofen", "tylenol", "razor"
            )
        },
        // Baby
        "Baby" to { name ->
            name.matchesAny(
                "diaper", "baby food", "formula", "baby wipes", "baby", "sippy cup",
                "pacifier"
            )
        },
        // Pet
        "Pet" to { name ->
            name.matchesAny(
                "dog food", "cat food", "pet food", "cat litter", "dog treat",
                "cat treat", "pet"
            )
        },
        // Peanut Butter & Spreads
        "Peanut Butter & Spreads" to { name ->
            name.matchesAny(
                "peanut butter", "jelly", "jam", "honey", "nutella", "syrup",
                "maple syrup", "preserves", "marmalade"
            )
        },
        // Cookies & Candy
        "Cookies & Candy" to { name ->
            name.matchesAny(
                "cookie", "cookies", "oreo", "candy", "chocolate", "gummy",
                "gummies", "m&m", "skittles", "snickers", "reese"
            )
        }
    )

    fun infer(normalizedName: String): String? {
        for ((category, matcher) in categoryRules) {
            if (matcher(normalizedName)) {
                return category
            }
        }
        return null
    }

    private fun String.matchesAny(vararg keywords: String): Boolean {
        return keywords.any { keyword ->
            this.contains(keyword)
        }
    }
}
