package com.example.cosc341_recycling_sorting_project.ui.quiz;

import com.example.cosc341_recycling_sorting_project.R;

import java.util.*;

public class QuestionBank {

    public static final String KELOWNA_RECYCLING = "Kelowna Recycling Quiz";

    private static final Map<String, List<Question>> DATA = new HashMap<>();

    static {
      //  int img = R.drawable.recyclinglogo; // use any quiz image you add

        DATA.put(KELOWNA_RECYCLING, Arrays.asList(
                // Q1
                new Question(
                        "Which of the following items should not be placed loose in your blue-lidded curbside recycling cart in Kelowna?",
                     //   img,
                        Arrays.asList(
                                "Clean and empty plastic container",
                                "Flattened cardboard box",
                                "Styrofoam (white polystyrene)",
                                "Rinsed tin can"
                        ),
                        2
                ),
                // Q2
                new Question(
                        "In Kelowna’s curbside program, how should materials be placed in the blue recycling cart?",
                      //  img,
                        Arrays.asList(
                                "In tied plastic garbage bags",
                                "Loose and unbagged",
                                "Packed into cardboard boxes only",
                                "Only in clear recycling bags"
                        ),
                        1
                ),
                // Q3
                new Question(
                        "What should you do with non-deposit glass bottles and jars from your home in Kelowna?",
                      //  img,
                        Arrays.asList(
                                "Put them in the blue recycling cart",
                                "Put them in the garbage cart",
                                "Take them to a recycling depot",
                                "Leave them beside the cart in a cardboard box"
                        ),
                        2
                ),
                // Q4
                new Question(
                        "Which of the following is allowed in your curbside yard-waste cart?",
                     //   img,
                        Arrays.asList(
                                "Grass clippings",
                                "Plastic bags filled with leaves",
                                "Dirt and rocks",
                                "Kitchen food scraps"
                        ),
                        0
                ),
                // Q5
                new Question(
                        "Which item must stay out of the yard-waste cart under RDCO rules?",
                     //   img,
                        Arrays.asList(
                                "Small branches under 5 cm in diameter",
                                "Leaves",
                                "Pumpkins",
                                "Animal feces or cat litter"
                        ),
                        3
                ),
                // Q6
                new Question(
                        "If you have more garbage than fits in your cart with the lid closed, what does the Tag-a-Bag program allow?",
                     //   img,
                        Arrays.asList(
                                "Put unlimited extra bags beside your cart",
                                "Put up to two extra bags with paid Tag-a-Bag stickers",
                                "Leave loose garbage on top of the cart",
                                "Put extra garbage into the blue recycling cart"
                        ),
                        1
                ),
                // Q7
                new Question(
                        "If your curbside carts are missed on collection day in Kelowna, who should you contact first?",
                     //   img,
                        Arrays.asList(
                                "City of Kelowna front desk",
                                "Environmental 360 Solutions (E360S)",
                                "RCMP non-emergency line",
                                "Recycle BC head office"
                        ),
                        1
                ),
                // Q8
                new Question(
                        "Which statement about filling your carts follows RDCO rules?",
                      //  img,
                        Arrays.asList(
                                "Carts can be overfilled as long as the driver can still tip them",
                                "Only the garbage cart needs the lid closed; recycling can overflow",
                                "All material must fit inside the cart so the lid can fully close",
                                "Yard-waste carts may be piled higher than the rim"
                        ),
                        2
                ),
                // Q9
                new Question(
                        "Which material should not go in the blue recycling cart and instead go to special disposal?",
                      //  img,
                        Arrays.asList(
                                "Clean paper and cardboard",
                                "Plastic containers from food products",
                                "Electronics or small appliances",
                                "Metal food cans"
                        ),
                        2
                ),
                // Q10
                new Question(
                        "Which statement best describes yard-waste collection for curbside homes in the Central Okanagan (including Kelowna)?",
                       // img,
                        Arrays.asList(
                                "Yard waste is picked up every week, all year",
                                "Yard waste is only accepted at depots, never at the curb",
                                "Yard waste is collected March through December, on alternating weeks with recycling",
                                "Yard waste is only collected during July"
                        ),
                        2
                )
        ));
    }

    public static List<Question> forCategory(String categoryTitle) {
        List<Question> list = DATA.get(categoryTitle);
        if (list == null) return Collections.emptyList();
        return new ArrayList<>(list);
    }
}