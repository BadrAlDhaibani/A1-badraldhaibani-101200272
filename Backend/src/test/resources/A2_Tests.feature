Feature: A2_Tests

  Scenario: A1_scenario
    Given A new game is started
    And the decks are created
    Then the hands of the 4 players are set up with random cards
    Then the 4 hands are rigged to hold the specific initial cards

    When P1 draws a quest of 4 stages
    And P1 is asked but declines to sponsor P2 is asked but agrees to sponsor
    Then P2 builds the 4 stages of the quest as specified

    # Stage 1
    Then P1 is asked and decides to participate and draws an F30 and discards an F5
    Then P3 is asked and decides to participate and draws a Sword and discards an F5
    Then P4 is asked and decides to participate and draws an Axe and discards an F5
    Then P1 builds an attack with Dagger and Sword total value 15
    Then P3 builds an attack with Sword and Dagger total value 15
    Then P4 builds an attack with Dagger and Horse total value 15
    Then all 3 players attacks are sufficient and proceed to the next stage
    And all 3 participants discard the cards used for their attacks1

    # Stage 2
    Then P1 is asked and decides to participate P1 draws a F10
    Then P3 is asked and decides to participate P3 draws a Lance
    Then P4 is asked and decides to participate P4 draws a Lance
    Then P1 builds an attack with Horse and Sword
    Then P3 builds an attack with Axe and Sword
    Then P4 builds an attack with Horse and Axe
    Then P1s attack is insufficient P1 loses and cannot go to the next stage
    And P1 has no shields and their hand is F5 F10 F15 F15 F30 Horse Axe Axe Lance
    And P3 and P4s attack are sufficient and proceed to the next stage
    And all 3 participants discard the cards used for their attacks2

    # Stage 3
    Then P3 is asked and decides to participate P3 draws an Axe
    Then P4 is asked and decides to participate P4 draws a Sword
    Then P3 builds an attack with Lance Horse and Sword
    Then P4 builds an attack with Axe Sword and Lance
    Then P3 and P4s attacks are sufficient and proceed to the next stage
    And All 2 participants discard the cards used for their attacks

    # Stage 4
    Then P3 is asked and decides to participate P3 draws a F30
    Then P4 is asked and decides to participate P4 draws a Lance
    Then P3 builds an attack with Axe Horse and Lance
    Then P4 builds an attack with Dagger Sword Lance and Excalibur
    Then P3s attack is insufficient P3 loses and receives no shields
    And P4s attack is sufficient P4 receives 4 shields
    And All 2 participants discard the cards used for their attacks2
    And P3 has no shields and has the cards F5 F5 F15 F30 Sword
    And P4 has 4 shields and has the cards F15 F15 F40 Lance

